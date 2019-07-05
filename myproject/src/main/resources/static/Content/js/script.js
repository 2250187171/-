window.onload=function(){
    var oWrap=document.getElementById('wrap');   
    var oUl=oWrap.getElementsByTagName('ul')[0];  //获取元素ul
    var oLi=oWrap.getElementsByTagName('li');    //获取全部的li,oLi 是一个数组
    var oBox=document.getElementById('box');
    var timer=null;//定义一个空对象
    var speed=-2;
	        oUl.innerHTML=oUl.innerHTML+oUl.innerHTML;  //把oUl的内容复制一次

    function move(){   //移动

        oUl.style.width=oLi[0].offsetWidth*oLi.length+'px';   //给oUl加一个行内样式的宽度  复制之后，ul宽度一开始没有设置，而外层的DIV 只有600PX,所以要设置 UL里面有多少个LI 就设置多少宽度
        timer=setInterval(function(){
            if(oUl.offsetLeft<-oUl.offsetWidth/2){      // 往左走    ul,已经复制了一份，现在里面是8张图片了，当走到UL一半的距离的时候，就把UL拉回来
                oUl.style.left=0;
            }
            if(oUl.offsetLeft>0){    //往右走
                oUl.style.left=-oUl.offsetWidth/2+'px';
            }
            oUl.style.left=oUl.offsetLeft+speed+'px';
         
        },30);
    };
    move();
/*    prevBtn.onclick=function(){
        speed=-2;
    };
    nextBtn.onclick=function(){
        speed=2;
    };
*/ 
   oBox.onmouseover=function(){
        clearInterval(timer);    //清除定时器
    };
    oBox.onmouseout=function(){
        move();
    }
}