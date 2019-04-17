var Host = "http://localhost:8888";
//登录判断
function loginCheck(){
	$.ajax({ 
	    type: "post", 
	    url: Host+'/user/uuid', 
	    async:false, 
	    dataType: "json", 
        success: function(response){ 
        	//如果用户没有登录，则跳转登录
            if(response.code == 0){
            	window.location.href=Host+"/play/login";
            }
        } 
	});
}