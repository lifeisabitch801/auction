/*guestbook*/
var name = "给自己取个喜欢的昵称吧";
	$("#name").val(name).bind("focus",function(){
		if(this.value == name){
			this.value = "";
			this.className = "focus_text"
		}
	}).bind("blur",function(){
		if(this.value == ""){
			this.value = name;
			this.className = "cz-birds"
		}
	});
var tel = "我们不会透露您的任何信息";
	$("#tel").val(tel).bind("focus",function(){
		if(this.value == tel){
			this.value = "";
			this.className = "focus_text"
		}
	}).bind("blur",function(){
		if(this.value == ""){
			this.value = tel;
			this.className = "cz-birds"
		}
	});
var email = "留下您的邮箱，关注我们的动态";
	$("#email").val(email).bind("focus",function(){
		if(this.value == email){
			this.value = "";
			this.className = "focus_text"
		}
	}).bind("blur",function(){
		if(this.value == ""){
			this.value = email;
			this.className = "cz-birds"
		}
	});
var msg = "文明发言，拒绝网络低俗文化！";
	$("#msg").val(msg).bind("focus",function(){
		if(this.value == msg){
			this.value = "";
			this.className = "focus_text"
		}
	}).bind("blur",function(){
		if(this.value == ""){
			this.value = msg;
			this.className = "cz-birds"
		}
	});
	/*guestbook*/