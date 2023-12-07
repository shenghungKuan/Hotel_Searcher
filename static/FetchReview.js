 function fetchReview(id) {
 console.log(id);
 if (id == "") {
 	    document.getElementById("reviews").innerHTML = "No hotel selected ";
 	    return;
     }
     fetch('/hotel?hotelId=' + id, {method :'get'}).
     then(res => res.text()).
     then(data => {
         document.getElementById("reviews").innerHTML = data;

      }).
     catch(err => {
       console.log(err);
     });
 };