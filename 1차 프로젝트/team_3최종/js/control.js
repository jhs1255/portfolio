//조작키 버튼 변수정의
let rightPressed = false;
let leftPressed = false;

let isBallReleased = false; // 발사 상태 변수
let isGameRunning = false; // 게임 실행 상태 변수
let isSetting = false; //설정창 열림 상태 변수

//키보드 조작
document.addEventListener("keydown", keyDownHandler, false);
document.addEventListener("keyup", keyUpHandler, false);

//키보드 이벤트 등록
function keyDownHandler(e) {
    if (e.key === "d" || e.key === "ArrowRight") {
      rightPressed = true;
    } else if (e.key === "a" || e.key === "ArrowLeft") {
      leftPressed = true;
    } else if (e.key === "Escape") {
      togglePause();
  }
}
  
function keyUpHandler(e) {
    if (e.key === "d" || e.key === "ArrowRight") {
      rightPressed = false;
    } else if (e.key === "a" || e.key === "ArrowLeft") {
      leftPressed = false;
    }
}

//버튼 동작시 실행

document.querySelector("#start").addEventListener("click",function(){
  if (!isGameRunning) {
    isGameRunning = true;
    intervalid = setInterval(draw, 10);
    document.querySelector("#start").disabled = true;
  }
});

document.querySelector("#stop").addEventListener("click",function(){
    document.location.reload();
    clearInterval(intervalid);
    isGameRunning = false;
    document.querySelector("#start").disabled = false;
    document.location.reload();
});

document.addEventListener("keydown",function(e){
    if(e.key === ' '){ 
        if (!isGameRunning) {
          isGameRunning = true;
          intervalid = setInterval(draw, 10);
          document.querySelector("#start").disabled = true;
        }
    }
});

document.querySelector("#home").addEventListener('click',function(){
    location.href="./index.html";
});

//게임 일시 정지 및 재개
function togglePause() {
    if (isGameRunning && isSetting ==false) {
      clearInterval(intervalid);
      isGameRunning = false;
      $('#pauseModal').modal('show');
    } else if(isSetting == false) {  
        intervalid = setInterval(draw, 10);
        isGameRunning = true;
    }
}

document.querySelector("#resumeButton").addEventListener("click", function () {
  togglePause();
});

document.querySelector("#settingsButton").addEventListener("click", function () {
    $('#settingsModal').modal('show');
    isSetting =true;
});

document.querySelector("#homeButton").addEventListener("click", function () {
  document.location.href = 'index.html';
});