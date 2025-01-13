// 점수 출력할 span 객체
let scoreDisplay = document.querySelector("#score");

// 점수.
let score = 0;

// 점수 더하는 함수.
function addScore(value) {
    score += value;
}

// 점수 출력 함수.
function printScore() {
    scoreDisplay.innerText = score;
}

// 로컬 스트리지에서 최고점수 받아오기
let highscoreDisplay = document.querySelector("#highscore");

// 페이지 로드 시 최고 점수 표시
// ★ ball.js의 onload로 이동.

// 최고 점수 표시 함수
function displayHighscore() {
    let scores = JSON.parse(localStorage.getItem('scores')) || [];
    
    if (scores.length > 0) {
        let highestScore = scores[0].score;
        highscoreDisplay.innerText = highestScore;
    } else {
        highscoreDisplay.innerText = 0; // 저장된 점수가 없을 경우 기본값 0으로 표시
    }
}