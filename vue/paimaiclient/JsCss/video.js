$(document).ready(function(){
	// 切换视频
	$(".video-list li").click(function(){
		var obj = $(this);
		$("#video_introduction").html(obj.attr("title"));
		var video_id = obj.attr("video");
		var path = obj.attr("id");
		var vid = obj.attr("vid");
		$(".js_videoCon").hide();
		$("#js_videoCon_"+vid).show();
		obj.addClass("now").siblings().removeClass("now");
	});
	
	//上下移动
	var n=0;
	var leng=$(".video-list ul li").size();
	$("#up").click(function(){
			n++;
			if(n<=leng-6)
			{
				$(".video-list ul").animate({'top':-62*n+'px'});
				$("#up").attr('class','up2')
				$("#down").attr('class','down2')
			}
			else
			{
				n=n-1;
				$("#up").attr('class','up')
			}
		})
	
	$("#down").click(function(){
			n--;
			if(n>=0)
			{
				$(".video-list ul").animate({'top':-62*n+'px'});
				$("#up").attr('class','up2')
				$("#down").attr('class','down2')
			}
			else
			{
				n=n+1;
				$("#down").attr('class','down')
			}
		})	
});