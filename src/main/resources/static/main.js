main.js_onchange_imageURL추가.js
function validateFileInput() {
    const fileInput = document.getElementById('fileInput');
    const filePath = fileInput.value;
    const allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;

    if (!filePath) {
       alert('파일을 선택해 주세요.');
       return false;
    }

    if (!allowedExtensions.exec(filePath)) {
       alert('이미지 파일만 업로드할 수 있습니다. (jpg, jpeg, png, gif)');
       fileInput.value = '';
       return false;
    }
    return true;
 }
/* $(document).ready(function () {
    $('#uploadForm').submit(function (event) {
       event.preventDefault(); // 기본 동작 중단

       const fileInput = document.getElementById('fileInput');
       const filePath = fileInput.value;
       const allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;

       if (!filePath) {
          alert('파일을 선택해 주세요.');
          return false;
       }

       if (!allowedExtensions.exec(filePath)) {
          alert('이미지 파일만 업로드할 수 있습니다. (jpg, jpeg, png, gif)');
          fileInput.value = '';
          return false;
       }

       // 파일 업로드 시작 전에 로딩 화면을 보여줍니다.
       showLoading();

       // AJAX를 사용하여 파일 업로드 처리
       $.ajax({
          url: '/uploadfile',
          type: 'POST',
          data: new FormData($('#uploadForm')[0]),
          processData: false,
          contentType: false,
          success: function (response) {
             // 업로드 성공 후 처리
             window.location.href = '/result'; // 결과 페이지로 이동
          },
          error: function (xhr, status, error) {
             // 업로드 실패 시 처리
             alert('파일 업로드 중 오류가 발생했습니다.');
             hideLoading();
          }
       });

       return true;
    });

    function showLoading() {
       // 로딩 화면을 보여주는 코드
       window.location.href = '/loading.html';
    }
    function hideLoading() {
       // 로딩 화면을 숨기는 코드
       $('#loading').hide();
    }
 });*/

 function readURL(input) {
   if (input.files && input.files[0]) {
     var reader = new FileReader();
     reader.onload = function(e) {
       document.getElementById('preview').src = e.target.result;
     };
     reader.readAsDataURL(input.files[0]);
   } else {
     document.getElementById('preview').src = "";
   }
 }