 function fetchReview(id) {
 console.log(id);
     fetch('/showreview?hotelId=' + id + '&page=0', {method :'get'}).
     then(res => res.text()).
     then(data => {
         document.getElementById("reviews").innerHTML = data;
      }).
     catch(err => {
       console.log(err);
     });
 };
 function recordHistory(id) {
         fetch('/history?hotelId=' + id, {method: 'get'});
     };