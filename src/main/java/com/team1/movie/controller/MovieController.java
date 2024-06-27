package com.team1.movie.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.movie.entity.ImageFile;
import com.team1.movie.service.ImageService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MovieController {

	@Autowired
	ImageService imageService;

	@GetMapping("/")
	public String uploadfileForm() {

		return "main";
	}

	@PostMapping("/")
	public String uploadfileProcess(@RequestParam("file") MultipartFile file, HttpSession session, Model model) {
		try {
			// 파일 유형 검사
			if (!file.getContentType().startsWith("image/")) {
				model.addAttribute("error", "이미지 파일만 업로드할 수 있습니다.");
//				return "test/req_form";
				return "main";
			}
			System.out.println(file.getSize());
			// 파일 유효성 체크: 이미지 파일인지 확인

//             URL url = new URL("http://3.35.81.123:5000/analyze_img");
			URL url = new URL("http://127.0.0.1:5000/analyze_img");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			String boundary = UUID.randomUUID().toString();

			// 파일 저장
			file.getOriginalFilename();
			ImageFile imageFile = new ImageFile();
			imageFile.setId(boundary);
			imageFile.setPhoto(file.getBytes());
			System.out.println("원본사이즈:" + file.getSize());
			System.out.println("원본:" + file.getBytes().toString());
			imageService.insert(imageFile);
			///////////////////////////////
			// HTTP 연결 설정
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setRequestProperty("content-Type", "multipart/form-data;boundary=" + boundary);
			// HTTP 요청 데이터 작성
			OutputStream out = con.getOutputStream();
			DataOutputStream request = new DataOutputStream(out);
			request.writeBytes("--" + boundary + "\r\n");
			request.writeBytes("Content-Disposition: form-data; name=\"data\"\r\n\r\n");
//			request.writeBytes(data + "\r\n");

			request.writeBytes("--" + boundary + "\r\n");
			request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"");
			request.writeBytes(file.getOriginalFilename() + "\"\r\n\r\n");
			request.write(file.getBytes());
			request.writeBytes("\r\n");

			request.writeBytes("--" + boundary + "--\r\n");
			request.flush();
			int respCode = con.getResponseCode();

			// 요청결과 코드에 따른 처리
			switch (respCode) {
			case 200:
				System.out.println("OK");
				break;
			case 301:
			case 302:
			case 307:
				System.out.println("Redirect");
				break;
			default:
			}

			// 요청 후 응답 결과 받기 위한 코드 작성
			InputStream in = con.getInputStream();
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			BufferedReader response = new BufferedReader(reader);

			String str = null;
			StringBuffer buff = new StringBuffer();
			while ((str = response.readLine()) != null) {
				buff.append(str + "\n");
			}

			String result = buff.toString().trim();

			// 결과 문자열을 Map 객체로 변환
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> map = mapper.readValue(result, Map.class);

			System.out.println(map.keySet().toString());
			Set<String> keys = map.keySet();
			Iterator<String> itor = keys.iterator();
			while (itor.hasNext()) {
				String k = itor.next();
				// String v = map.get(k);
				if (k.equals("result_list")) {
					Object v = map.get(k);
					System.out.println(k + ":" + v);
				}
			}

			// 불러오기
			String id = boundary;
			String ended64 = Base64.getEncoder().encodeToString(imageService.getImageById(id).getPhoto());

			session.setAttribute("imgSrc", ended64); // 이미지 데이터를 base64로 변환하여 src에 설정
			session.setAttribute("reqResult", map);

			// 결과가 준비되지 않았을 때는 waiting 페이지로 리다이렉트
			return "redirect:/waiting";
		} catch (Exception e) {
			return "errorPage";
		}

	}

	@GetMapping("/waiting")
	public String showWaitingPage() {
		return "loading";
	}

	@GetMapping("/result")
	public String showResultPage(HttpSession session, Model model) {
		// 세션에서 데이터 가져오기
		String imgSrc = (String) session.getAttribute("imgSrc");
		Map<String, String> reqResult = (Map<String, String>) session.getAttribute("reqResult");

		// 세션에 저장된 데이터가 없는 경우 에러 페이지로 리다이렉트
		if (imgSrc == null || reqResult == null) {
			return "redirect:/error";
		}

		// 모델에 데이터 추가
		model.addAttribute("imgSrc", imgSrc);
		model.addAttribute("reqResult", reqResult);

		return "result";
	}

	@ExceptionHandler(RuntimeException.class)
	public String handleRuntimeException(RuntimeException e, Model model) {
//		model.addAttribute("errorMessage", e.getMessage());
		return "errorPage";
	}

	@GetMapping("/faq")
	public String siteabout() {
		return "SiteAbout";
	}

	@GetMapping("/checkResult")
	@ResponseBody
	public ResponseEntity<Void> checkResult(HttpSession session) {
		// 세션에서 결과 가져오기
		Map<String, String> reqResult = (Map<String, String>) session.getAttribute("reqResult");

		// 결과가 준비되었는지 확인
		if (reqResult != null) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

}
