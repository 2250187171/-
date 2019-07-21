function DINGWEI(){
// 百度地图API功能
    var map = new BMap.Map("allmap");
    //创建geolocation对象
    var geolocation = new BMap.Geolocation();
    //创建地址解析的实例
    var geoc = new BMap.Geocoder();
    //返回用户的当前位置信息
    geolocation.getCurrentPosition(function(r){

        var point=new BMap.Point(r.point.lng+0.0364319,r.point.lat+0.01371587);
        //返回完成后的状态码
        if(this.getStatus() == BMAP_STATUS_SUCCESS){
            //对指定的坐标点进行反地址解析
            geoc.getLocation(point, function(rs){
                //addressComponents：结构化的地址描述
                $("#location").val(rs.address);
            });
        }else {
            alert('failed'+this.getStatus());
        }
    })
}

//打卡位置
function mapAddress(address,title){
    // 百度地图API功能
    var map = new BMap.Map("allmap");
    // var point = new BMap.Point(116.331398,39.897445);
    // map.centerAndZoom(point,12);
    // 创建地址解析器实例
    var myGeo = new BMap.Geocoder();
    // 将地址解析结果显示在地图上,并调整地图视野
    myGeo.getPoint(address, function(point){
        if (point) {
            var myIcon = new BMap.Icon("/gateway/attend/Content/images/hc_meitu_1.png", new BMap.Size(50,50));
            map.centerAndZoom(point, 16);
            //创建标注
            var marker=new BMap.Marker(point,{icon:myIcon});
            //将标注添加到地图中
            map.addOverlay(marker);
            // marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
            var opts = {
                width : 200,     // 信息窗口宽度
                height: 50,     // 信息窗口高度
                title : title , // 信息窗口标题
                enableMessage:true,//设置允许信息窗发送短息
                message:"当前地址"
            }
            var infoWindow = new BMap.InfoWindow("车辆位置:"+address, opts);  // 创建信息窗口对象
            marker.addEventListener("click", function(){
                map.openInfoWindow(infoWindow,point); //开启信息窗口
            });
        }else{
            alert("您选择地址没有解析到结果!");
        }
    },"武汉市");

}