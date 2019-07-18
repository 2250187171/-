var TabRoute;
$(function() {
    TabRoute = $('#TabRoute')
        .DataTable(
            {
                responsive: true,
                "pagingType" : "simple_numbers",//设置分页控件的模式
                searching : false,//屏蔽datatales的查询框
                // aLengthMenu : [ count ],//设置一页展示10条记录
                "bSort" : false,// 是否排序功能
                "bLengthChange" : true,//屏蔽tables的一页展示多少条记录的下拉列表
                "bAutoWidth" : false,// 是否非自动宽度  设置为false
                "oLanguage" : { //对表格国际化
                    "sLengthMenu" : "每页显示 _MENU_条",
                    "sZeroRecords" : "没有找到符合条件的数据",
                    //  "sProcessing": "&lt;img src=’./loading.gif’ /&gt;",
                    "sInfo" : "当前第 _START_ - _END_ 条　共计 _TOTAL_ 条",
                    "sInfoEmpty" : "木有记录",
                    "sInfoFiltered" : "(从 _MAX_ 条记录中过滤)",
                    "sSearch" : "搜索：",
                    "sProcessing" : "正在加载中......",
                    "oPaginate" : {
                        "sFirst" : "首页",
                        "sPrevious" : "前一页",
                        "sNext" : "后一页",
                        "sLast" : "尾页"

                    }
                },
                "lengthMenu" : [ 2, 5, 10, 20, 50 ],
                "processing" : true, //打开数据加载时的等待效果
                "serverSide" : true,//打开后台分页
                "ajax" : {
                    "url" : "/gateway/admin/siteManage/findIFRoute",
                    "dataSrc" : "aaData",
                    "data" : function(d) {
                        d.ccityID=$("#ccity").val();
                        d.cprovinceID=$("#cprovince").val();
                        d.cdistrictID= $("#cdistrict").val();
                        d.dcityID=$("#dcity").val();
                        d.dprovinceID=$("#dprovince").val();
                        d.ddistrictID=$("#ddistrict").val();
                    }
                },
                "columns" : [
                    {
                        "data" : null,
                        "width" : "60px",
                        render:function(data,type,row,meta) {
                            return meta.row + 1 +
                                meta.settings._iDisplayStart;
                        }
                    },
                    {
                        "data" : "routeStart",
                        "width" : "50px"
                    },{
                        "data" : "routeEnd",
                        "width" : "50px"
                    },
                    {
                        "data" : "userName",
                        "width" : "50px"
                    },
                    {
                        "data" : "phoneNumber",
                        "width" : "40px"
                    },
                    {

                        "data" : "FreightRate",
                        "width" : "50px"

                    },
                    {
                        "data" : "Prescription",
                        "width" : "50px"
                    },
                    {
                        "data" : function(obj) {
                            return '<button class="btn btn-info AuditRecord" value="'+obj.routeStart+"="+obj.routeEnd+'" onclick="bdMap(this.value)" id="ditu"><i class="layui-icon">&#xe715;</i> 查看地图</button>&nbsp;&nbsp;'
                                +'<button class="btn btn-warning" value="'
                                + obj.RouteID + '"onclick="updateUser(this.value)" id="update">修改</button>&nbsp;&nbsp;'
                                +'<button class="btn btn-danger" onclick="deleteRoute('+obj.RouteID+')" id="update">删除</button>'
                        },
                        "width" : "80px"

                    } ]

            });
    ajaxUser();

});
//不现实错误提示框
$.fn.dataTable.ext.errMode = 'none';
function Change() {
    TabRoute.ajax.reload();
}

//动态加载联系人下拉框
function ajaxUser(){
    //清空联系人列表的选项，只保留标题
    $("#userName")[0].options.length=1;//只保留标题
    //动态加载联系人下拉框
    $.ajax({
        url:"/gateway/admin/userManage/findByRoleID",
        data:{"roleID":4},
        type:"GET",
        dataType:'json',
        success:function (data) {
            $(data).each(function () {
                $("<option value='"+this.userID+"'>"+this.userName+"</option>").appendTo("#userName");
            })
        }
    })
}
//查询下拉框事件------------------------------------------
//出发下拉框触发------------
//当省份的下拉框的值发生变化时
$("#cprovince").change(function () {
    var proID=$("#cprovince").val();
    //清空城市列表的选项，只保留标题
    $("#ccity")[0].options.length=1;//只保留标题
    $("#cdistrict")[0].options.length=1;//只保留标题
    ajaxCity("#ccity",proID,0);
})

//当城市的下拉框的值发生变化时
$("#ccity").change(function () {
    var cityID=$("#ccity").val();
    //清空城市列表的选项，只保留标题
    $("#cdistrict")[0].options.length=1;//只保留标题
    ajaxDistrict("#cdistrict",cityID,0);
})

//到达下拉框事件------------------------------------------
//当省份的下拉框的值发生变化时
$("#dprovince").change(function () {
    var proID=$("#dprovince").val();
    //清空城市列表的选项，只保留标题
    $("#dcity")[0].options.length=1;//只保留标题
    $("#ddistrict")[0].options.length=1;//只保留标题
    ajaxCity("#dcity",proID,0);
})

//当城市的下拉框的值发生变化时
$("#dcity").change(function () {
    var cityID=$("#dcity").val();
    //清空城市列表的选项，只保留标题
    $("#ddistrict")[0].options.length=1;//只保留标题
    ajaxDistrict("#ddistrict",cityID,0);
})
var PANDUAN=1;
//新增下拉框事件-----------------------------------------------------------------

//加载市
function ajaxCity(Id,proID,ccityID){
    $.post("/gateway/admin/siteManage/findByProID",{"proID":proID},function (data) {
        $(data).each(function () {
            if(this.cityID==ccityID){
                $("<option value='"+this.cityID+"' selected>"+this.cityName+"</option>").appendTo(Id);
            }else
            {
                $("<option value='"+this.cityID+"'>"+this.cityName+"</option>").appendTo(Id);
            }
        })
    },"json")
}
//加载区县
function ajaxDistrict(Id,cityID,ddisID){
    $.post("/gateway/admin/siteManage/findByCityID",{"cityID":cityID},function (data) {
        $(data).each(function () {
            if(this.disID==ddisID){
                $("<option value='"+this.disID+"' selected>"+this.disName+"</option>").appendTo(Id);
            }else
            {
                $("<option value='"+this.disID+"'>"+this.disName+"</option>").appendTo(Id);
            }
        })
    },"json")
}


//出发地下拉框触发----------------------
//当省份的下拉框的值发生变化时
$("#addcprovince").change(function () {
    var proID=$("#addcprovince").val();
    //清空城市列表的选项，只保留标题
    $("#addccity")[0].options.length=1;//只保留标题
    $("#addcdistrict")[0].options.length=1;//只保留标题
    ajaxCity("#addccity",proID,0);
})

//当城市的下拉框的值发生变化时
$("#addccity").change(function () {
    var cityID=$("#addccity").val();
    //清空城市列表的选项，只保留标题
    $("#addcdistrict")[0].options.length=1;//只保留标题
    ajaxDistrict("#addcdistrict",cityID,0);
})
//到达地下拉框触发----------------------
//当省份的下拉框的值发生变化时
$("#adddprovince").change(function () {
    var proID=$("#adddprovince").val();
    //清空城市列表的选项，只保留标题
    $("#adddcity")[0].options.length=1;//只保留标题
    $("#adddcistrict")[0].options.length=1;//只保留标题
    ajaxCity("#adddcity",proID,0);
})

//当城市的下拉框的值发生变化时
$("#adddcity").change(function () {
    var cityID=$("#adddcity").val();
    //清空城市列表的选项，只保留标题
    $("#addddistrict")[0].options.length=1;//只保留标题
    ajaxDistrict("#addddistrict",cityID,0);
})
//---------------------------------------------------------------》
//点击新增按钮弹出新增表单
function add(){
    PANDUAN=1;
    document.getElementById("myModalLabel").innerText="新增路线";
    $("#addform").attr("action","/gateway/admin/siteManage/addRoute");
    $('#myModal').modal({
        backdrop: false
    });
}

//新增路线
$("#btn_submit").click(function () {
    //获得出发的市
    var addccity=$("#addccity").val();
    //获得到达的市
    var adddcity=$("#adddcity").val();
    //获得联系人
    var userName=$("#userName").val();
    //获得运价
    var FreightRate=$("#FreightRate").val();
    //获得时效
    var Prescription=$("#Prescription").val();
    if(addccity=='0'||adddcity=='0'||userName=='0'||FreightRate==''||FreightRate==null||Prescription==''||Prescription==null){
        layer.msg('请填写完整信息', {
            icon: 2,
            time: 1000
        })
    }else {
        var index = layer.load(1); //添加laoding,0-2两种方式
        //提交表单
        $("#addform").ajaxSubmit(function (data) {
            layer.close(index);
            if (PANDUAN==1){
                if(data=='1'||data==1){
                    layer.msg("新增成功", {
                        icon: 1,
                        time: 1000
                    },function () {
                        $('#myModal').modal('hide');
                        $("#addform input").val("");
                        $("#addform select").val(0);
                        Change();
                    })
                }else {
                    layer.msg("新增失败", {
                        icon: 2,
                        time: 1000
                    })
                }
            } else {
                if(data=='1'||data==1){
                    layer.msg("修改成功", {
                        icon: 1,
                        time: 1000
                    },function () {
                        $('#myModal').modal('hide');
                        $("#addform input").val("");
                        $("#addform select").val(0);
                        Change();
                    })
                }else {
                    layer.msg("修改失败", {
                        icon: 2,
                        time: 1000
                    })
                }
            }
        })
    }

})

//关闭弹出框
$("[name=btn_close]").click(function () {
    $('#myModal').modal('hide');
    $("#addform input").val("");
    //清空城市列表的选项，只保留标题
    $("#addccity")[0].options.length=1;//只保留标题
    $("#addcdistrict")[0].options.length=1;//只保留标题
    $("#adddcity")[0].options.length=1;//只保留标题
    $("#addddistrict")[0].options.length=1;//只保留标题
    $("#addform select").val(0);
})

//修改路线
function updateUser(RouteID){
    PANDUAN=2;
    //请求获得路线的信息
    $.ajax({
        url:"/gateway/admin/siteManage/findById",
        type: "get",
        dataType: "json",
        data:{"RouteID":RouteID},
        success:function (data) {
            ajaxCity("#addccity",data.cproID,data.ccityID);
            ajaxDistrict("#addcdistrict",data.ccityID,data.routeStartID);
            ajaxCity("#adddcity",data.dproID,data.dcityID);
            ajaxDistrict("#addddistrict",data.dcityID,data.routeEndID);
            $("#routeID").val(data.RouteID)
            $("#addcprovince").val(data.cproID);
            $("#adddprovince").val(data.dproID);
            $("#userName").val(data.userID);
            $("#FreightRate").val(data.FreightRate);
            $("#Prescription").val(data.Prescription);
        }
    })
    document.getElementById("myModalLabel").innerText="修改路线";
    $("#addform").attr("action","/gateway/admin/siteManage/updateRoute");
    $('#myModal').modal({
        backdrop: false
    });
}

//删除路线
function deleteRoute(RouteID){
    //配置一个透明的询问框
    layer.msg('确定要删除吗？', {
        time: 40000, //20s后自动关闭
        btn: ['确定', '取消'],
        yes: function(index, layero){
            layer.close(index); //如果设定了yes回调，需进行手工关闭
            $.ajax({
                url:"/gateway/admin/siteManage/deleteRoute",
                data:{"RouteID":RouteID},
                type:"get",
                dataType:"json",
                success:function (data) {
                    if(data=='1'||data==1){
                        layer.msg("删除成功", {
                            icon: 1,
                            time: 1000
                        },function () {
                            Change();
                        })
                    }else
                    {
                        layer.msg("删除失败", {
                            icon: 2,
                            time: 1000
                        })
                    }
                }
            })

        }
    });
}

