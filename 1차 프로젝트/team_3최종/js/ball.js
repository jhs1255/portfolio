/*------------------------------------변수 선언---------------------------------------*/
const canvas = document.querySelector('#canvas');
const ctx = canvas.getContext("2d");
/*--------------------------------------------------------------------------------- */


// ★ 최고 점수 출력 추가.(score.js 에서 옮김.)
window.onload = function () {
    console.log("window.onload");

    // ★ 캔버스 크기 설정.
    canvas.width = 1200;
    canvas.height = 700;

    init();
    displayHighscore(); // 최고 점수 출력 추가.
}

// ★ 초기화 함수.
function init() {

    ball = new createBall(0,0,0,0,0,0); // 공 생성.
    initBall();                         // 공 설정 초기화.

    initBricks();   // 벽돌 설정 초기화.
    
    drawBricks();   // 벽돌 그리기.
    drawPaddle();   // 패들 그리기.
    drawBall();     // 공 그리기.
}

// ★ 공 초기화 함수.
function initBall() {
    // 위치 조정
    ball.posX = canvas.width / 2;
    ball.posY = canvas.height -20;
    
    // 공 속도 조정
    ball.speed = 4;

    // 공 방향(각도) 적용.
    let degree = Math.floor((Math.random() * 1000) % 160 + 10) + 180; // (10'~170'의 랜덤 각도로, y축 반전으로 180도 더해줌).
    let vector = convertDegreeToVector(degree); // 방향(각도)에 대한 단위벡터 구함.
    
    // 공의 방향에 공의 속도를 곱해줌.
    ball.speedX = vector.x * ball.speed;
    ball.speedY = vector.y * ball.speed;

    // 공 크기 조정
    ball.radius = 12;

    console.log(ball);
}

// ★ 공 객체.
let ball = {
    posX: 0,
    posY: 0,
    speedX: 0,
    speedY: 0,
    speed: 0,
    radius: 0,
};

// ★ 공 객체 생성 함수.
function createBall(posX, posY, speedX, speedY, speed, radius) {
    this.posX = posX;
    this.posY = posY;
    this.speedX = speedX;
    this.speedY = speedY;
    this.speed = speed;
    this.radius = radius;
}

//공이 떨어진 횟수
let ballFall = 0;

let ballColor = 'blue';
let paddleColor = 'white';

//목숨
function lifeDisplay(){
    const lifes = [document.querySelector('#life1'),document.querySelector('#life2'),document.querySelector('#life3')];
    lifes.forEach((life, index) => {
        if(index < ballFall){
            life.style.display = 'none';
        }else{
            life.style.display = 'inline-block';
        }
    });
}


let intervalid;

//바 크기 조정
let paddleHeight = 10; // 바의 세로 길이
let paddleWidth = 150; //바의 가로 길이
let paddleX = (canvas.width - paddleWidth) / 2; //바가 중앙으로 오도록 조정



//공 그리기
var color = Math.random() * 0xffffff
color = parseInt(color);
color = color.toString(16);
var colorCode = "#" +color;

function getRandomColor() {
	return "#" + Math.floor(Math.random() * 16777215).toString(16);
}


function drawBall() {
    ctx.beginPath();
    // ★ 공 객체 적용.
    ctx.arc(ball.posX, ball.posY, ball.radius, 0, Math.PI * 2);
    ctx.fillStyle = ballColor; //색상 고정
    //ctx.fillStyle = colorCode; //랜덤한 색상 
    //ctx.fillStyle = getRandomColor();//계속 색상 변화
    ctx.fill();
    ctx.closePath();
}

// 공 초기화 함수 (바닥에 떨어졌을때 상태 초기화하는 함수.)
function resetBall() {
    // ★ 공 초기화 해주고,
    initBall();

    // ★ 패들 초기화 해주고,
    paddleWidth = 150;
    paddleX = (canvas.width - paddleWidth) / 2;
    
    // ★ 생명 다시 표시해줌.
    lifeDisplay();
}


//바 그리기
function drawPaddle() {
    ctx.beginPath();
    ctx.rect(paddleX, canvas.height - paddleHeight, paddleWidth, paddleHeight);
    ctx.fillStyle = paddleColor;
    ctx.fill();
    ctx.closePath();
}


function draw(){
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    drawBall();
    drawPaddle();
    drawBricks();
    drawItemsFalling();
    upditemfall();
    checkPaddleItem();
    collider();

    //충돌 감지 (벽면, 천장, 바닥 충돌 감지)// ★  공 객체 적용.
    // 좌우 벽 충돌 감지 부분.
    if (ball.posX + ball.speedX > canvas.width - ball.radius || ball.posX + ball.speedX < ball.radius) {
        ball.speedX = -ball.speedX; //좌우 충돌 감지
    }
    // 천장 충돌 감지 부분.
    if(ball.posY + ball.speedY < ball.radius) {
        ball.speedY = -ball.speedY; //상하 충돌 감지
    }
    // 바닥 충돌 감지 부분.
    else if(ball.posY + ball.speedY > canvas.height-ball.radius) {  
        // 패들 충돌 감지 부분.
        if(ball.posX > paddleX && ball.posX < paddleX + paddleWidth) {//공이 바 가장자리에 닿았는지 확인

            // ★ 패들 충돌 시 전반사가 아닌 (10~170)의 각으로 랜덤 반사.
            let degree = Math.floor((Math.random() * 1000) % 160 + 10) + 180;
            // 반사할 각도를 단위 벡터로 변환.
            let vector = convertDegreeToVector(degree);
            
            // 반사 방향에 현재 공의 속도를 곱해줌.
            ball.speedX = vector.x * ball.speed;
            ball.speedY = vector.y * ball.speed;
        }
        // 패들이 아닌 바닥 충돌 감지 부분.
        else {
            ballFall++; // 공이 바닥에 닿을 때마다 카운트 증가
            if (ballFall < 3) {
                resetBall(); // 3번 미만으로 떨어졌을 경우 공 초기화
            } else { 
                clearInterval(intervalid); // 3번 이상으로 떨어졌을 경우 게임 종료
                gameOver();
            }
        }
    }

    if(rightPressed && paddleX < canvas.width-paddleWidth) {
        paddleX += 7;
    }
    else if(leftPressed && paddleX > 0) {
        paddleX -= 7;
    }

    // ★ 공 객체 적용.
    ball.posX += ball.speedX;
    ball.posY += ball.speedY;
}

draw();