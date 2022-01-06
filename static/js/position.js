function getPositions() {
  var criteria = new Criteria();

  if (!verifyMACAdress(criteria.terminalId))
    getPositionsErrorHandler("Invalid MAC Adress");
  else if (criteria.startDate === undefined || criteria.endDate === undefined)
    getPositionsErrorHandler("Start Date or End Date is undefined");
  else
    sendRequest(
      "GET",
      criteria.terminalId + "/" + criteria.startDate + "/" + criteria.endDate,
      null,
      getPositionsSuccessHandler,
      getPositionsErrorHandler
    );
}

function verifyMACAdress(MACAdress) {
  // Regex to check valid
  // MAC address
  const regex = new RegExp("([0-9A-Fa-f]{2}[:-]){6}");

  return regex.test(MACAdress);
}

class Criteria {
  constructor() {
    var deviceId = $("#deviceId").val().trim(); // select data from input and trim it
    if (deviceId.length > 0) {
      this.terminalId = deviceId;
    }

    var startDate = $("#startDate").val().trim(); // select data from input and trim it
    if (startDate.length > 0) {
      this.startDate = startDate;
    }

    var endDate = $("#endDate").val().trim(); // select data from input and trim it
    if (endDate.length > 0) {
      this.endDate = endDate;
    }
  }
}

function getPositionsSuccessHandler(respData) {
  $("#result").append("<br>" + JSON.stringify(respData));
  localStorage.setItem('locations', JSON.stringify(respData))
}

function getPositionsErrorHandler(status) {
  alert("err response: " + status); // popup on err.
}
