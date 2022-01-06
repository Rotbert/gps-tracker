function goToPage(url) {
  var criteria = new Criteria();
  if (criteria.username === "admin" && criteria.password === "admin")
    $(location).attr("href", url);
  else {
    alert("Invalid username or password!");
  }
}

class Criteria {
  constructor() {
    var user = $("#username").val().trim(); // select data from input and trim it
    if (user.length > 0) {
      this.username = user;
    }

    var pass = $("#password").val().trim(); // select data from input and trim it
    if (pass.length > 0) {
      this.password = pass;
    }
  }
}
