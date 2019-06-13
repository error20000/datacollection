var baseUrl = '../';
var changePwdUrl = baseUrl + "api/user/changePWD";
var logoutUrl = baseUrl + "api/user/logout";
var isLoginUrl = baseUrl + "api/user/isLogin";


new Vue({
    el: '#app',
    data() {
        return {
          sysName: "后台管理",
          sysUserName: "",
          menus: [],
      	  preloading: false,
          //pwd
          pwdFormVisible: false,
          pwdLoading: false,
          pwdFormRules: {
            oldPwd: [
              { required: true, message: "请输入旧密码.", trigger: "blur" }
            ],
            newPwd: [{ required: true, message: "请输入新密码.", trigger: "blur" }],
            newPwd2: [
              { required: true, message: "再次输入新密码.", trigger: "blur" },
              {
                validator: (rule, value, callback) => {
                  if (value !== this.pwdForm.newPwd) {
                    callback(new Error("密码不一致!"));
                  } else {
                    callback();
                  }
                },
                trigger: "blur"
              }
            ]
          },
          pwdForm: {
            oldPwd: "",
            newPwd: "",
            newPwd2: ""
          }
        };
      },
      methods: {
        handleSelect: function(index) {
          this.showIframe(index);
        },
		showIframe: function(index){
			let url = "";
			
			switch (Number(index)) {
			case 1:
				url = 'beacon.html';
				break;
			case 2:
				url = 'data.html';
				break;
			case 3:
				break;
			case 4:
				break;
				
			default:
				break;
			}
			$('.content-iframe').attr('src', url);
		},
        //update pwd
        handlepwdChange: function() {
          this.pwdFormVisible = true;
          this.pwdForm = {
            oldPwd: "",
            newPwd: "",
            newPwd2: ""
          };
        },
        pwdChangeClose: function() {
          this.pwdFormVisible = false;
          this.pwdLoading = false;
          this.$refs.pwdForm.resetFields();
        },
        pwdChange: function() {
          this.$refs.pwdForm.validate(valid => {
            if (valid) {
              this.$confirm('确定提交吗?', '提示', {}).then(() => {
                var params = Object.assign({}, this.pwdForm);
                delete params.newPwd2;
                var self = this;
                this.pwdLoading = true;
                ajaxReq(changePwdUrl, params, function(res) {
                  self.pwdLoading = false;
                  if (res.code > 0) {
                    self.$message({
                      message: "成功",
                      type: "success"
                    });
                    self.addFormVisible = false;
                    localStorage.removeItem('loginUser');
                    parent.window.location.href = "login.html";
                  }else if(res.code == -206){
					self.$message({
							message: '缺少参数.',
							type: 'warning'
						})
					}else if(res.code == -213){
						self.$message({
							message: '密码错误. ',
							type: 'warning'
						})
					}else if(res.code == -111){
						self.$message({
							message: '未登录. ',
							type: 'warning'
						})
					}else{
						self.$message({
							message: '失败',
							type: 'warning'
						})
					}
                });
              });
            }
          });
        },
        //login
        logout: function() {
          this.$confirm("确定退出系统吗？", "提示", {
            //type: 'warning'
          }).then(() => {
              var self = this;
              var params = {};
              ajaxReq(logoutUrl, params, function(res) {
                if (res.code > 0) {
                	parent.window.location.href = "login.html";
                }else{
                	self.$message({
						message: '失败',
						type: 'warning'
					});
                }
              });
            }).catch(() => {});
        },
        isLogin: function(cb) {
			var params = {};
			ajaxReq(isLoginUrl, params, function(res){
				if(res.code <= 0){
					localStorage.removeItem('loginUser');
					parent.window.location.href = "login.html";
				}else{
					if(typeof cb == 'function'){
						cb(res.data);
					}
					
				}
			});
        },
        initLoginUser: function(data){
        	this.user = data;
      		this.sysUserName = this.user.username;
        }
      },
      mounted: function() {
      	this.user = JSON.parse(localStorage.getItem('loginUser'));
  		if(this.user　==　null){
  			parent.window.location.href = "login.html";
  			return;
  		}
    	loginUserId = this.user.pid;
		this.isLogin(this.initLoginUser);
		this.preloading = true;
      }
  });
