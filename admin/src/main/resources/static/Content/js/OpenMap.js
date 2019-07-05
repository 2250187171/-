var map = new BMap.Map("dituContent");// 在百度地图容器中创建一个地图
var myXWeiZi = "";
var myYWeiZi = "";
var Searchlocation = 0;

WeiZhi = new Array();

var PanDuan = 0;
var TimeOut;
// 创建和初始化地图函数：
function initMap() {
	layer.load();
	createMap();// 创建地图
	setMapEvent();// 设置地图事件
	addMapControl();// 向地图添加控件
	getlocation();
}

// 创建地图函数：
function createMap() {

	var point = new BMap.Point(116.401137, 39.913807);// 定义一个中心点坐标
	map.centerAndZoom(point, 7);// 设定地图的中心点和坐标并将地图显示在地图容器中
	window.map = map;// 将map变量存储在全局
}
function G(id) {
	return document.getElementById(id);
}
// 获取本地的位置
function getlocation() {

	TimeOut = setInterval(function() {
		PanDuan++;
		if (PanDuan == 5) {
			clearInterval(TimeOut);
			BrowserGetLocation();
			layer.closeAll('loading');
			layer.msg("定位你所在的位置失败，请刷新界面！！", {
				icon : 0,

				offset : '100px',

			});
		}
	}, 4000);

	var geolocation = new BMap.Geolocation(); // 实例化浏览器定位对象。
	// 下面是getCurrentPosition方法。调用该对象的getCurrentPosition()，与HTML5不同的是，这个方法原型是getCurrentPosition(callback:function[,
	// options: PositionOptions])，也就是说无论成功与否都执行回调函数1，第二个参数是关于位置的选项。
	// 因此能否定位成功需要在回调函数1中自己判断。
	geolocation.getCurrentPosition(function(r) { // 定位结果对象会传递给r变量
		if (this.getStatus() == BMAP_STATUS_SUCCESS) { // 通过Geolocation类的getStatus()可以判断是否成功定位。
			var mk = new BMap.Marker(r.point); // 基于定位的这个点的点位创建marker
			map.addOverlay(mk); // 将marker作为覆盖物添加到map地图上
			map.panTo(r.point); // 将地图中心点移动到定位的这个点位置。注意是r.point而不是r对象。
			// alert('您的位置：' + r.point.lng + ',' + r.point.lat);
			// //r对象的point属性也是一个对象，这个对象的lng属性表示经度，lat属性表示纬度。
			myXWeiZi = r.point.lng;
			myYWeiZi = r.point.lat;
			if (myXWeiZi != "" && myYWeiZi != "") {
				GetLocalInformation();
			} else {
				BrowserGetLocation();

			}
		} else {
			BrowserGetLocation();

		}
	}, {
		enableHighAccuracy : true
	})

}

// 地图事件设置函数：
function setMapEvent() {
	map.enableDragging();// 启用地图拖拽事件，默认启用(可不写)
	map.enableScrollWheelZoom();// 启用地图滚轮放大缩小
	map.enableDoubleClickZoom();// 启用鼠标双击放大，默认启用(可不写)
	map.enableKeyboard();// 启用键盘上下左右键移动地图
}

function GetLocalInformation() {
	var point = new BMap.Point(myXWeiZi, myYWeiZi);// 用所定位的经纬度查找所在地省市街道等信息
	var gc = new BMap.Geocoder();
	gc.getLocation(point, function(rs) {
		var addComp = rs.addressComponents;
		// console.log(rs.address);//地址信息
		// alert(rs.address);//弹出所在地址
		var adress = addComp.streetNumber;
		if (adress > 0) {
			DiZhi = rs.address + "号";
		} else {
			DiZhi = rs.address;
		}
		if (DiZhi == "") {
			DiZhi = "网络错误，识别不了地址！！";
		}
		if (Institutionalorganizationid == 3) {

			$.post("/LogisticsSystemSSM/TransportController/dingWei.do?DiZhi="
					+ DiZhi);
		}
		if (adress != null && adress != "") {
			$("#location").val(rs.address + "号");

		} else {
			$("#location").val(rs.address);
		}
		clearInterval(TimeOut);
		nowAdress = rs.address;
		map.centerAndZoom(point, 15);
		layer.closeAll('loading');
	});
}

// 地图控件添加函数：
function addMapControl() {
	// 向地图中添加缩放控件
	var ctrl_nav = new BMap.NavigationControl({
		anchor : BMAP_ANCHOR_TOP_LEFT,
		type : BMAP_NAVIGATION_CONTROL_LARGE
	});
	map.addControl(ctrl_nav);
	// 向地图中添加缩略图控件
	var ctrl_ove = new BMap.OverviewMapControl({
		anchor : BMAP_ANCHOR_BOTTOM_RIGHT,
		isOpen : 1
	});
	map.addControl(ctrl_ove);
	// 向地图中添加比例尺控件
	var ctrl_sca = new BMap.ScaleControl({
		anchor : BMAP_ANCHOR_BOTTOM_LEFT
	});
	map.addControl(ctrl_sca);
}

function serachlocal(diDian) {

	if (WeiZhi.length == 4 && Searchlocation == 0) {
		WeiZhi.splice(0, WeiZhi.length);
	}

	var local = new BMap.LocalSearch(map, {
		renderOptions : {
			map : map,
			// panel: "results",//结果容器id
			autoViewport : true, // 自动结果标注
			selectFirstResult : false
		// 不指定到第一个目标
		},
		pageCapacity : 8,
		// 自定义搜索回调数据
		onSearchComplete : function(results) {
			if (local.getStatus() == BMAP_STATUS_SUCCESS) {

				for ( var i = 0; i < results.getCurrentNumPois(); i++) {

					var poi = results.getPoi(i);

					if (poi.point.lng > 0 && poi.point.lat > 0) {
						if (Searchlocation == 0) {

							// WeiZhi = poi.point.lng + "," + poi.point.lat;
							WeiZhi.push(poi.point.lng);
							WeiZhi.push(poi.point.lat);

						} else {
							myXWeiZi = poi.point.lng;
							myYWeiZi = poi.point.lat;
						}
						break;
					} else {
						layer.msg("定位失败", {
							icon : 0,

							offset : '100px'
						});
					}

				}

			}

		},

	});

	local.search(diDian);

}
// 定义一个控件类,即function
function ZoomControl() {
	this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
	this.defaultOffset = new BMap.Size(10, 10);
}

// 通过JavaScript的prototype属性继承于BMap.Control
ZoomControl.prototype = new BMap.Control();

// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
// 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
ZoomControl.prototype.initialize = function(map) {
	// 创建一个DOM元素
	var div = document.createElement("div");
	div.innerHTML = '<div id="r-result"><input type="text" id="suggestId"placeholder="搜索地址"  size="20"  style="width:300px;"class="form-control" /></div><div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>';

	// 添加DOM元素到地图中
	map.getContainer().appendChild(div);

	// 将DOM元素返回
	return div;
}

// 创建控件
var myZoomCtrl = new ZoomControl();
// 添加到地图当中
map.addControl(myZoomCtrl);

var ac = new BMap.Autocomplete( // 建立一个自动完成的对象
{
	"input" : "suggestId",
	"location" : map
});

ac.addEventListener("onhighlight", function(e) { // 鼠标放在下拉列表上的事件
	var str = "";
	var _value = e.fromitem.value;
	var value = "";
	if (e.fromitem.index > -1) {
		value = _value.province + _value.city + _value.district + _value.street
				+ _value.business;
	}
	str = "FromItem<br />index = " + e.fromitem.index + "<br />value = "
			+ value;

	value = "";
	if (e.toitem.index > -1) {
		_value = e.toitem.value;
		value = _value.province + _value.city + _value.district + _value.street
				+ _value.business;
	}
	str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = "
			+ value;
	G("searchResultPanel").innerHTML = str;
});

var myValue;
ac.addEventListener("onconfirm", function(e) { // 鼠标点击下拉列表后的事件
	var _value = e.item.value;
	myValue = _value.province + _value.city + _value.district + _value.street
			+ _value.business;
	G("searchResultPanel").innerHTML = "onconfirm<br />index = " + e.item.index
			+ "<br />myValue = " + myValue;

	setPlace();
});

function setPlace() {
	Searchlocation = 1;
	map.clearOverlays(); // 清除地图上所有覆盖物
	function myFun() {
		var pp = local.getResults().getPoi(0).point; // 获取第一个智能搜索的结果
		map.centerAndZoom(pp, 14);
		map.addOverlay(new BMap.Marker(pp)); // 添加标注
	}
	var local = new BMap.LocalSearch(map, { // 智能搜索
		onSearchComplete : myFun
	});
	local.search(myValue);
}

// 地图事件设置函数：
function setMapEvent() {
	map.enableDragging();// 启用地图拖拽事件，默认启用(可不写)
	map.enableScrollWheelZoom();// 启用地图滚轮放大缩小
	map.enableDoubleClickZoom();// 启用鼠标双击放大，默认启用(可不写)
	map.enableKeyboard();// 启用键盘上下左右键移动地图
}

function BrowserGetLocation() {

	var options = {
		enableHighAccuracy : true, // boolean 是否要求高精度的地理信息，默认为false
		maximumAge : 1000
	// 应用程序的缓存时间
	}

	if (navigator.geolocation) {
		// 浏览器支持geolocation
		navigator.geolocation.getCurrentPosition(onSuccess, onError, options);

	} else {
		// 浏览器不支持geolocation
		console.log("浏览器不支持!");
	}

}

// 成功时
function onSuccess(position) {
	// 返回用户位置
	// 经度
	var longitude = position.coords.longitude;
	// 纬度
	var latitude = position.coords.latitude;
	var point = new BMap.Point(longitude, latitude);// 用所定位的经纬度查找所在地省市街道等信息
	myXWeiZi = longitude;
	myYWeiZi = latitude;
	if (myXWeiZi != "" && myYWeiZi != "") {
		GetLocalInformation();
	} else {
		layer.closeAll('loading');
		layer.msg("定位失败", {
			icon : 0,

			offset : '100px'
		});
	}
}

// 失败时
function onError(error) {
	switch (error.code) {
	case error.PERMISSION_DENIED:
		layer.msg("用户拒绝对获取地理位置的请求", {
			icon : 0,

			offset : '100px'
		});
		break;

	case error.POSITION_UNAVAILABLE:
		layer.msg("位置信息是不可用的", {
			icon : 0,

			offset : '100px'
		});
		break;

	case error.TIMEOUT:
		layer.msg("请求用户地理位置超时", {
			icon : 0,

			offset : '100px'
		});
		break;

	case error.UNKNOWN_ERROR:
		layer.msg("未知错误", {
			icon : 0,

			offset : '100px'
		});
		break;
	}
	layer.closeAll('loading');
	layer.msg("定位失败", {
		icon : 0,

		offset : '100px'
	});
}
