
$(function(){
	
	//首页大图左右切换效果
	var oUL=$("#benner_pic ul");
	var iSize=oUL.find("li").length;
	var iWidth=oUL.find("li").width();
	oUL.width(iSize*iWidth);
	var index=0;
	

	$("#in_RightBtn").click(function(){
		index=index+1;
		if(index>iSize-1){index=0}
		Switching2(index);
		})
	
	$("#in_LeftBtn").click(function(){
		index=index-1;
		if(index<0){index=iSize-1}
		Switching2(index);
		})


	//自动切换画面
	function zdqh(){
		time=setInterval(function(){
			if(index>iSize-1){index=0}
			Switching2(index);
			index=index+1;
			},2000)
		}
	
	zdqh();	
	
	
	
	
	//鼠标经过停止自动切换鼠标移开恢复切换
	$("#in_RightBtn").hover(function(){
		clearInterval(time);
		},
		function(){
			zdqh();
			}
		)
	
	//鼠标经过停止自动切换鼠标移开恢复切换
	$("#in_LeftBtn").hover(function(){
		clearInterval(time);
		},
		function(){
			zdqh();
			}
		)	

	function Switching2(index){
		oUL.animate({left:-iWidth*index})
	}


//首页小图左右切换效果

var len=$(".pro_s ul li").length;
var width=$(".pro_s ul li").width();
var sindex=0;




//上下一张按钮
$(".left_btn").click(function(){
	sindex=sindex-1;
	if(sindex<0){sindex=len-1}
	Switching(sindex);
	})


$(".right_btn").click(function(){
	sindex=sindex+1;
	if(sindex>len-1){sindex=0}
	Switching(sindex);
	})
	

 
//画面切换

function Switching(sindex){
	$(".pro_s ul").animate({left:-width*sindex});
	}

})
//后端项目的地址
var baseurl = "http://localhost:8088/";
var imgurl = "http://localhost:8081/upload/";
var wsurl = "ws://localhost:8080/"
//默认地址为后端项目的地址
axios.defaults.baseURL = baseurl;
axios.defaults.withCredentials = true;
axios.defaults.headers.common["token"]=sessionStorage.getItem("token");
axios.interceptors.response.use(resp =>{
	console.log(resp.data);
	if(resp.data.code=="401"){
		location.href="login.html"
	}else{
		return resp;
	}
	},
	error=>{
})