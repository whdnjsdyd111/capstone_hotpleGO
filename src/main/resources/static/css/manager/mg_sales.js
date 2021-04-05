let bg = ['rgba(255, 99, 132, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(255, 206, 86, 0.2)',
    'rgba(75, 192, 192, 0.2)', 'rgba(153, 102, 255, 0.2)', 'rgba(255, 159, 64, 0.2)'];
let backcolor = [];

let border = ['rgba(255,99,132,1)', 'rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)',
    'rgba(75, 192, 192, 1)', 'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)'];
let bordercolor = [];

for (let i = 0; i < 30; i++) {
    backcolor.push(bg[i % bg.length]);
    bordercolor.push(border[i % border.length]);
}

$(function() {
    const dates = Object.keys(map);
    const sales = [];
    dates.forEach(date => {
        let sum = 0;
        map[date].forEach(m => sum += m.mePrice * m.rsMeNum);
        sales.push(sum);
    });

    const maxSale = Math.max(...sales);
    const minSale = Math.min(...sales);
    const maxDate = dates[sales.findIndex(i => i === maxSale)];
    const minDate = dates[sales.findIndex(i => i === minSale)];
    let sum = 0;
    sales.forEach(i => sum += i);
    const avg = sum / sales.length;

    const allDates = [];
    allDates.push(...dates);
    const allSales = [];
    allSales.push(...sales);
    const weekDates = [];
    const weekSales = [];
    const monthDates = [];
    const monthSales = [];

    let today = new Date();
    const sevenAgo = new Date(new Date().setDate(today.getDate() - 6));

    for (let i = 0; i < 7; i++) {
        weekDates.push(dateToStr(sevenAgo));
        let index = allDates.indexOf(weekDates[i]);
        if (index === -1) weekSales.push(0);
        else weekSales.push(allSales[index]);
        sevenAgo.setDate(sevenAgo.getDate() + 1);
    }

    const monthAgo = new Date(new Date().setDate(today.getDate() - 30));

    for (let i = 0; i < 30; i++) {
        monthDates.push(dateToStr(monthAgo));
        let index = allDates.indexOf(monthDates[i]);
        if (index === -1) monthSales.push(0);
        else monthSales.push(allSales[index]);
        monthAgo.setDate(monthAgo.getDate() + 1);
    }

    today = dateToStr(today);

    $('#max').text(maxSale);
    $('#min').text(minSale);
    $('#maxDate').text(maxDate);
    $('#minDate').text(minDate);
    $('#sum').text(sum);
    $('#avg').text(avg);

    $('#today').click(function() {
        let index = dates.indexOf(today);
        let sale;
        let day = [today];
        if (index === -1) sale = [0];
        else sale = [sales[index]];
        init(day, sale);
    });

    $('#all').click(function() {
        init(allDates, allSales);
    });

    $('#week').click(function() {
        init(weekDates, weekSales);
    });

    $('#month').click(function() {
        init(monthDates, monthSales);
    });

    const ctx = document.getElementById("sales-chart").getContext('2d');
    const myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: '일별 바',
                data: sales,
                backgroundColor: backcolor,
                borderColor: bordercolor,
                borderWidth: 1
            }, {
                label: '일별 라인',
                type: 'line',
                fill: false,
                data: sales,
                backgroundColor: backcolor,
                borderColor: bordercolor
            }]
        },
        options: {
            maintainAspectRatio: true, // default value. false일 경우 포함된 div의 크기에 맞춰서 그려짐.
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero:true
                    }
                }]
            }
        }
    });


    function init(date, sale) {
        sales.length = 0;
        dates.length = 0;
        sales.push(...sale);
        dates.push(...date);
        myChart.update();
    }

    $('input[type=date]').change(function() {
        let s = $('#start').val();
        let e = $('#end').val();
        let tempDates = [];
        let tempSales = [];
        if (s && e) {
            if (s > e) {
                alert('날짜 설정을 제대로 시도해주십시오.');
                return;
            }
            let startDate = parse(s.replaceAll('-', ''));
            let endDate = parse(e.replaceAll('-', ''));

            let loop = (endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24;

            for (let i = 0; i <= loop; i++) {
                tempDates.push(dateToStr(startDate));
                let index = allDates.indexOf(tempDates[i]);
                if (index === -1) tempSales.push(0);
                else tempSales.push(allSales[index]);
                startDate.setDate(startDate.getDate() + 1);
            }

            init(tempDates, tempSales);
        }
    });
});

function dateToStr (date) {
    return date.getFullYear().toString().substring(2, 4) +
        addZero(date.getMonth() + 1) + addZero(date.getDate());
}

function parse(str) {
    let y = str.substr(0, 4);
    let m = str.substr(4, 2);
    let d = str.substr(6, 2);
    return new Date(y,m-1,d);
}

function addZero(month) {
    if (month < 10) return '-0' + month;
    else return '-' + month;
}
