function initClock() {
    const container = document.getElementById("time-container");
    const dateText = container.children[0].textContent.trim();
    const timeText = container.children[1].textContent.trim();

    const dateTimeString = `${dateText}T${timeText}`;

    return new Date(dateTimeString);
}

let currentTime = initClock();

function updateClock() {
    currentTime.setSeconds(currentTime.getSeconds() + 1);

    let hours = currentTime.getHours();
    let minutes = currentTime.getMinutes();
    let seconds = currentTime.getSeconds();
    hours = hours < 10 ? "0" + hours : hours;
    minutes = minutes < 10 ? "0" + minutes : minutes;
    seconds = seconds < 10 ? "0" + seconds : seconds;
    let timeString = `${hours}:${minutes}:${seconds}`;

    let year = currentTime.getFullYear();
    let month = currentTime.getMonth() + 1;
    let day = currentTime.getDate();
    month = month < 10 ? "0" + month : month;
    day = day < 10 ? "0" + day : day;
    let dateString = `${year}-${month}-${day}`;

    const container = document.getElementById("time-container");
    container.children[0].innerHTML = dateString;
    container.children[1].innerHTML = timeString;
}

setInterval(updateClock, 1000);
updateClock();
