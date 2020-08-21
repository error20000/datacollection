var baseUrl = parent.window.baseUrl || '../';

var queryUrl = baseUrl + "api/beacon/findPage";
var addUrl = baseUrl + "api/beacon/add";
var modUrl = baseUrl + "api/beacon/update";
var delUrl = baseUrl + "api/beacon/delete";
var picDirUrl = baseUrl + "api/beacon/picDirs";
var picUrl = baseUrl + "api/beacon/pics";
var rePicUrl = baseUrl + "api/beacon/refreshPic";
var reDataUrl = baseUrl + "api/beacon/refreshData";

var ajaxReq = parent.window.ajaxReq || "";


var myvue = new Vue({
	    el: '#app',
	    data: function(){
	    	return {
	    		activeTab: 'table',
				filters: {
					user: '',
					alarm: '',
					connected: '',
					status: ''
				},
				list: [],
				total: 0,
				page: 1,
				rows: 10,
				listLoading: false,
				sels: [],
				preloading: false,
				connectedOptions: [
					{value: "Y", label: "曾连接"},
					{value: "N", label: "未连接"},
				],
				statusOptions: [
					{value: "Y", label: "正常"},
					{value: "N", label: "异常"},
				],
				alarmOptions: [
					{value: "0", label: "正常"},
					{value: "1", label: "距离S1报警"},
					{value: "2", label: "距离S2报警"},
					{value: "3", label: "距离S3报警"},
					{value: "4", label: "距离S4报警"},
					{value: "5", label: "角度AX报警"},
					{value: "6", label: "角度AY报警"},
				],

				//add
				addFormVisible: false,
				addLoading: false, 
				addForm: {},
				addFormRules: {
		              sn: [
		                { required: true, message: '请输入序列号.', trigger: 'blur' },
		              ]
				},
				//edit
				editFormVisible: false,
				editLoading: false,
				editForm: {},
				editFormRules: {},
				//picDir
				picDirTitle: "",
				picDirFormVisible: false,
				picDirLists: [],
				//pic
				picTitle: "",
				picFormVisible: false,
				picLists: [],
				
				user: ''
			}
		},
		methods: {
			formatDate: function(date){
				return parent.window.formatDate(date, 'yyyy-MM-dd HH:mm:ss');
			},
			handleSizeChange: function (val) {
				this.rows = val;
				this.getList();
			},
			handleCurrentChange: function (val) {
				this.page = val;
				this.getList();
			},
			query:function(){
				this.page = 1;
				this.getList();
			},
			//query
			getList: function () {
				var self = this;
				var params = {
					page: this.page,
					rows: this.rows
				};
				for ( var key in this.filters) {
					if(this.filters[key]){
						params[key] = this.filters[key];
					}
				}
				this.listLoading = true;
				ajaxReq(queryUrl, params, function(res){
					self.listLoading = false;
					self.handleResQuery(res, function(){
						self.total = res.total;
						self.list = res.data;
						/*if(self.page != 1 && self.total <= (self.page - 1) * self.rows){
							self.page = self.page - 1;
							self.getList();
						}*/
					});
				});
			},
			//reset
			reset: function(){
				this.filters = {
					user: '',
					connected: '',
					status: ''
				};
				this.page = 1;
				this.getList();
			},
			//add
			handleAdd: function(){
				this.addFormVisible = true;
				this.addForm = {
						name: '',
						sn: '',
						connected: 'N',
						status: 'N'
				};
			},
			addClose: function () {
				this.addFormVisible = false;
				this.addLoading = false;
				this.$refs.addForm.resetFields();
			},
			addSubmit: function () {
				this.$refs.addForm.validate((valid) => {
					if (valid) {
						this.$confirm('确定提交吗?', '提示', {}).then(() => {
							var params = Object.assign({}, this.addForm);
							var self = this;
							this.addLoading = true;
							ajaxReq(addUrl, params, function(res){
								self.addLoading = false;
								self.handleResOperate(res, function(){
									self.addFormVisible = false;
									self.getList();
								});
							});
						});
					}
				});
			},
			handleDel: function(index, row){
				this.$confirm('确定删除该条记录吗? ', '提示', {
					type: 'warning'
				}).then(() => {
					var self = this;
					this.listLoading = true;
					ajaxReq(delUrl, {pid: row.pid }, function(res){
						self.listLoading = false;
						self.handleResOperate(res, function(){
							self.getList();
						});
					});
					
				}).catch(() => {
				});
			},
			//edit
			handleEdit: function (index, row) {
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
			},
			editClose: function () {
				this.editFormVisible = false;
				this.editLoading = false;
				this.$refs.editForm.resetFields();
			},
			editSubmit: function () {
				this.$refs.editForm.validate((valid) => {
					if (valid) {
						this.$confirm('确认提交吗?', '提示', {}).then(() => {
							var self = this;
							this.editLoading = true;
							var params = Object.assign({}, this.editForm);
							ajaxReq(modUrl, params, function(res){
								self.editLoading = false;
								self.handleResOperate(res, function(){
									self.editFormVisible = false;
									self.getList();
								});
							});
							
						});
					}
				});
			},
			//picDir
			handlePicDir: function (index, row) {
				this.picDirTitle = row.sn;
				this.picDirFormVisible = true;
				this.picDirLists = [];
				var self = this;
				ajaxReq(picDirUrl, {sn: row.sn }, function(res){
					self.handleResQuery(res, function(){
						let list = res.data;
						//order
						for (var i = 0; i < list.length; i++) {
							for (var j = i; j < list.length; j++) {
								if(list[i].name > list[j].name){
									let temp = list[i];
									list[i] = list[j];
									list[j] = temp;
								}
							}
						}
						//foreach
						for (var i = 0; i < list.length; i++) {
							self.picDirLists.push({
								name: list[i].name,
								sn: row.sn,
								dir: list[i].name,
								date: self.formatDate(new Date(list[i].time))
							});
						}
					});
				});
			},
			picDirClose: function(){
				this.picDirFormVisible = false;
			},
			//pic
			handlePic: function (sn, dir) {
				this.picTitle = sn + " > " + dir;
				this.picFormVisible = true;
				this.picLists = [];
				var self = this;
				ajaxReq(picUrl, {sn: sn, dir: dir }, function(res){
					self.handleResQuery(res, function(){
						let list = res.data;
						//order
						for (var i = 0; i < list.length; i++) {
							for (var j = i; j < list.length; j++) {
								if(list[i].time < list[j].time){
									let temp = list[i];
									list[i] = list[j];
									list[j] = temp;
								}
							}
						}
						//foreach
						for (var i = 0; i < list.length; i++) {
							self.picLists.push({
								name: list[i].name,
								uri: '/upload/'+sn+'/'+dir+'/'+list[i].name,
								date: self.formatDate(new Date(list[i].time))
							});
						}
					});
				});
			},
			picClose: function(){
				this.picFormVisible = false;
			},
			//refresh
			handleRefreshPic: function (index, row) {
				var self = this;
				ajaxReq(rePicUrl, {sn: row.sn }, function(res){
					self.handleResQuery(res, function(){
						if(res.code > 0){
							self.$message({
								message: '成功发送查询图片指令。',
								type: 'success'
							});
						}else{
							self.$message({
								message: '失败',
								type: 'warning'
							});
						}
					});
				});
			},
			handleRefreshData: function (index, row) {
				var self = this;
				ajaxReq(reDataUrl, {sn: row.sn }, function(res){
					self.handleResQuery(res, function(){
						if(res.code > 0){
							self.$message({
								message: '成功发送查询检测数据指令。',
								type: 'success'
							});
						}else{
							self.$message({
								message: '失败',
								type: 'warning'
							});
						}
					});
				});
			},
			
			
			selsChange: function (sels) {
				this.sels = sels;
			},
			//res
			toLoginHtml: function(){
                localStorage.removeItem('loginUser');
                parent.window.location.href = "login.html";
			},
			handleResQuery: function(res, success, failed){
				this.handleRes(false, res, success, failed);
			},
			handleResOperate: function(res, success, failed){
				this.handleRes(true, res, success, failed);
			},
			handleRes: function(show, res, success, failed){
				if(res.code > 0){
					if(show){
						this.$message({
							message: '成功',
							type: 'success'
						});
					}
					if(typeof success == 'function'){
						success(res, this);
					}
				}else if(res.code == -111){
					this.$message({
						message: '未登录.',
						type: 'warning'
					});
					this.toLoginHtml();
				}else if(res.code == -201){
					this.$message({
						message: '没有权限.',
						type: 'warning'
					});
				}else{
					if(show){
						this.$message({
							message: '失败',
							type: 'warning'
						});
					}
					if(typeof failed == 'function'){
						failed(res, this);
					}
				}
			}
		},
		mounted: function() {
	    	try {
	    		this.user = JSON.parse(localStorage.getItem('loginUser'));
			} catch (e) {
				localStorage.removeItem("loginUser");
			}
	  		if(!this.user){
	  			parent.window.location.href = "login.html";
	  			return;
	  		}
			this.preloading = true;
			this.getList();
		}
	  });
	
	

