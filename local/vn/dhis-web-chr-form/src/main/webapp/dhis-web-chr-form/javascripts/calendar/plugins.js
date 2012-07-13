function fOnChange(y,m,d) {
	fFormatDate(y,m,d);
}
function fAfterSelected(y,m,d) {
}
// ====== Following are self-defined and/or custom-built functions! =======
var _dc, _mc, _yc;
function fshowCalendar(dc) {
  var range='';
  fPopCalendar(dc, range, "toggle");
}