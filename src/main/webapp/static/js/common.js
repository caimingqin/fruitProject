var commandHost="http://localhost/qiefou/commandHandler.shtml"
function command(commandName,datas,callback){

 var postData = { "commandName":commandName,
					"contents":JSON.stringify(datas)
						};	
            $.ajax({
                type: "POST",
                dataType: "json",
                url: commandHost,
                contentType: "application/json",
                data: JSON.stringify(postData),
                success: function (data) {
                console.log(data);
                if(callback!=null&&callback!=undefined){
                      callback(data);
                }
             },
                error: function () {
                    alert("404");
                }
            });
}
