var Host = "http://localhost:8888";
//登录判断
function loginCheck(callback){
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
            if(callback!=null) {
            	callback(response.data.uuid);
            }
        } 
	});
}

//获取url参数
function GetQueryString(name) {
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);//search,查询？后面的参数，并匹配正则
     if(r!=null)return  unescape(r[2]); return null;
}
//整形判断
function isInteger(obj) {
    return obj%1 === 0
}