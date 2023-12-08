function fetchWeather(lat, lng) {
console.log(lat);
     fetch('https://api.open-meteo.com/v1/forecast?latitude=' + lat + '&longitude=' + lng + '&current_weather=true', {method :'get'}).
     then(res => res.json()).
     then(data => data.current_weather).
     then(info => {
         document.getElementById("weather").innerHTML = 'temperature: ' + info.temperature + '<br>windspeed: ' + info.windspeed;
         console.log(info);
      }).
     catch(err => {
       console.log(err);
     });
 };