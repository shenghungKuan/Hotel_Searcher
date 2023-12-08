function addToFavorite(id) {
     fetch('/favorite?hotelId=' + id, {method :'get'}).
     catch(err => {
       console.log(err);
     });
     alert("Added this hotel to favorite!");
 };