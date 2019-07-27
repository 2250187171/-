layui.use('element', function() {
    var $ = layui.jquery
        , element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
    //触发事件
    var active = {
        tabAdd: function (url, id, name) {
            //新增一个Tab项
            element.tabAdd('demo', {
                title: name,
                content: '<iframe data-frameid="' + id + '" scrolling="yes" frameborder="0" src="' + url + '" height="875px" width="100%"></iframe>',
                id: id //规定好的id
            })
        }
        , tabChange: function (id) {
            //切换到指定Tab项
            element.tabChange('demo', id); //切换到：用户管理
        }

    };

    //新增选项卡
    $("[name=tab]").click(function(){
        var tabName = $(this).text();
        var url = $(this).attr("url");
        var id = $(this).attr("id");
        //这时会判断右侧.layui-tab-title属性下的有lay-id属性的li的数目，即已经打开的tab项数目
        if ($(".layui-tab-title li[lay-id]").length <= 0) {
            //如果比零小，则直接打开新的tab项
            active.tabAdd(url, id, tabName);
        } else {
            //否则判断该tab项是否以及存在

            var isData = false; //初始化一个标志，为false说明未打开该tab项 为true则说明已有
            $.each($(".layui-tab-title li[lay-id]"), function () {
                //如果点击左侧菜单栏所传入的id 在右侧tab项中的lay-id属性可以找到，则说明该tab项已经打开
                if ($(this).attr("lay-id") == id) {
                    isData = true;
                }
            })
            if (isData == false) {
                //标志为false 新增一个tab项
                active.tabAdd(url, id, tabName);
            }
        }
        //最后不管是否新增tab，最后都转到要打开的选项页面上
        active.tabChange(id);
    })

})
