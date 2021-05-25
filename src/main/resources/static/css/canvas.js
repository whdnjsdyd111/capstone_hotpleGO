let canvas = document.getElementById('bubble');
let canvasBg = document.getElementById('bubbleBg');

let ctx = canvas.getContext("2d");
let ctxBg = canvasBg.getContext("2d");

const Bubbles = [];
const BgBubbles = [];

class Bubble {
    constructor(color, ySpeed, canvas) {
        this.radius = Math.random() * 150 + 30;
        this.life = true;
        this.x = Math.random() * canvas.width;
        this.y = Math.random() * 20 + canvas.height + this.radius;
        this.vy = Math.random() * 0.0002 + 0.001 + ySpeed;
        this.vr = 0;
        this.vx = Math.random() * 4 - 2;
        this.color = color;
    }

    update() {
        this.vy += 0.00001;
        this.vr += 0.02;
        this.y -= this.vy;
        this.x += this.vx;
        if (this.radius > 1) {
            this.radius -= this.vr;
        }
        if (this.radius <= 1) {
            this.life = false;
        }
    }

    draw(currentCanvas) {
        currentCanvas.beginPath();
        currentCanvas.arc(this.x, this.y, this.radius, 0, 2 * Math.PI);
        currentCanvas.fillStyle = this.color;
        currentCanvas.fill();
    }
}

function addBubble() {
    Bubbles.push(new Bubble("rgb(255, 194, 194", 2, canvas));
}

function addBgBubble() {
    BgBubbles.push(new Bubble("rgb(255, 255, 255", 3, canvasBg));
}

function handleBubbles() {
    for (let i = Bubbles.length - 1; i >= 0; i--) {
        Bubbles[i].update();
        if (!Bubbles[i].life) {
            Bubbles.splice(i, 1);
        }
    }

    for (let i = BgBubbles.length - 1; i >= 0; i--) {
        BgBubbles[i].update();
        if (!BgBubbles[i].life) {
            BgBubbles.splice(i, 1);
        }
    }

    if (Bubbles.length < canvas.width / 4) {
        addBubble();
    }

    if (BgBubbles.length < canvas.width / 12) {
        addBgBubble();
    }
}

function animate() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctxBg.clearRect(0, 0, canvas.width, canvas.height);

    handleBubbles();

    for (let i = Bubbles.length - 1; i >= 0; i--) {
        Bubbles[i].draw(ctx);
    }

    for (let i = BgBubbles.length - 1; i >= 0; i--) {
        BgBubbles[i].draw(ctxBg);
    }

    requestAnimationFrame(animate);
}

function init() {
    canvas = document.getElementById('bubble');
    canvasBg = document.getElementById('bubbleBg');

    ctx = canvas.getContext("2d");
    ctxBg = canvasBg.getContext("2d");

    animate();
}

init();

window.addEventListener("resize", function () {
    let Bubbles = [];
    let bgBubbles = [];
});