var baseUrl = '../';
var loginUrl = baseUrl + 'api/user/login';

function ajaxReq(url, param, callback, cp){
	$.ajax({
		   dataType: "json",
		   type: "POST",
		   url: url,
		   data: param,
		   success: function(data){
				if (typeof callback === "function") {
					callback(data, cp);
				}
		   },
		   error: function(data){
		   }
		});
}

  new Vue({
      el: '#app',
      data: function() {
        return {
        	preloading: false,
            logining: false,
            ruleForm: {
              username: '',
              password: ''
            },
            rules: {
              username: [
                { required: true, message: '请输入用户名。', trigger: 'blur' },
              ],
              password: [
                { required: true, message: '请输入密码。', trigger: 'blur' },
              ]
            },
            checked: true
			
          }
      },
      methods: {
          handleSubmit: function(ev) {
            this.$refs.ruleForm.validate((valid) => {
              if (valid) {
                var self = this;
				this.logining = true;
				var params = {
						username: this.ruleForm.username,
						password: this.ruleForm.password
				};
				ajaxReq(loginUrl, params, function(res){
					self.logining = false;
					if(res.code > 0){
						localStorage.setItem('loginUser', JSON.stringify(res.data));
						window.location.href = 'index.html';
					}else if(res.code == -110){
						self.$message({
							message: '密码错误',
							type: 'warning'
						})
					}else if(res.code == -109){
						self.$message({
							message: '用户名不存在',
							type: 'warning'
						})
					}else{
						self.$message({
							message: '失败',
							type: 'warning'
						})
					}
				});
              } else {
                console.log('error submit!!');
                return false;
              }
            });
          }
      
        },
    	mounted: function() {
    		this.preloading = true;
    	}
    });
  
