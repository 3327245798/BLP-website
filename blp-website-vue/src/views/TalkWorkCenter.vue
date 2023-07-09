
<template>
<!-- 这个页面是用来创建帖子、词汇及句型的 -->
  <div class="total">
    <div class="header">
      <div class="header-left">
        <img :src="blpLogo" style="cursor:pointer"  @click="$router.push({name:'Home'})" />
        <a-divider type="vertical" />
        <span  >创作中心</span>
      </div>
      <div class="header-right">
        <!-- <img />
        <span>头像</span> -->
        <!--  <span @click="$router.push('talkWorkCenter')">创作中心</span> -->
          <a-avatar slot="avatar" :src="touxiang1" alt="Han Solo" />
      </div>
    </div>
    <div class="content">
      <div class="content-left">
        <a-dropdown>
         <a-button type="primary" block>
      内容创作
    </a-button>
          <a-menu slot="overlay">
            <a-menu-item>
              <a @click="$router.push('createPost')">帖子</a>
            </a-menu-item>
           <a-menu-item>
             <a @click="$router.push('createWord')">词汇</a>
             </a-menu-item>
             <a-menu-item>
             <a @click="$router.push('createSentence')">句型</a>
             </a-menu-item>
          </a-menu>
        </a-dropdown>
      </div>
      <div class="content-right">
        <router-view></router-view>
      <!--   <a-form :form="postForm" class="formStyle">
          <a-form-item>
            <a-input
              class="title"
              v-decorator="[
                'postTitle',
                { rules: [{ required: false, message: '无' }] },
              ]"
              type="text"
              placeholder="请输入标题(最多50个字)"
            >
            </a-input>
          </a-form-item>
          <a-divider class="dividerStyle"></a-divider>
          <a-form-item>
            <a-textarea
              class="maintext"
              v-decorator="[
                'postMainText',
                { rules: [{ required: false, message: '无' }] },
              ]"
              placeholder="请输入正文"
              :auto-size="{ minRows: 15 }"
            />
          </a-form-item>
          <a-form-item>
            <a-upload
              list-type="picture-card"
              
              :file-list="fileList"
              @beforeUpload="handlebeforeUpload"
              @preview="handlePreview"
              @change="handleChange"
            >
          
              <div v-if="fileList.length < 1">
                <a-icon type="plus" />
                <div class="ant-upload-text">Upload</div>
              </div>
            </a-upload>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" block @click="submitpost()">
              发布
            </a-button>
          </a-form-item>
        </a-form> -->
      </div>
      
    </div>
    <div class="footer"></div>
  </div>
</template>

<script>
import blpLogo from "assets/blpLogo1.png";
import axios from "axios";
import PostService from "@/service/post_service";
import touxiang1 from "assets/touxiang1.jpg";
const postService = new PostService();
export default {
  data() {
    return {
      blpLogo,
      touxiang1,
      previewVisible: false,
      previewImage: "",
      postForm: this.$form.createForm(this, { name: 'coordinated' }),
      fileList: [
        /*   {
          uid: "-1",
          name: "image.png",
          status: "done",
          url: "https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png",
        },
        {
          uid: "-2",
          name: "image.png",
          status: "done",
          url: "https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png",
        },
        {
          uid: "-3",
          name: "image.png",
          status: "done",
          url: "https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png",
        },
        {
          uid: "-4",
          name: "image.png",
          status: "done",
          url: "https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png",
        },
        {
          uid: "-5",
          name: "image.png",
          status: "error",
        }, */
      ],
    };
  },
  created() {},
  watch: {},
  computed: {},
  methods: {
    //它默认的上传方式是加入图片后直接上传，返回false，可以自己手动上传。
    handlebeforeUpload(file) {
      //图片格式限制为 jpeg、png
      const isJpgOrPng =
        file.type === "image/jpeg" || file.type === "image/png";
      if (!isJpgOrPng) {
        this.$message.error("请选择jpg/png图片");
      }

      //图片大小限制为 2M以内
      const isLt2M = file.size / 1024 / 1024 <= 2;
      if (!isLt2M) {
        this.$message.error("图片大小不能超过2MB,请重新上传");
      }
      return isJpgOrPng && isLt2M;
    },

    getBase64(file) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = (error) => reject(error);
      });
    },
    handleCancel() {
      this.previewVisible = false;
    },
    async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await this.getBase64(file.originFileObj);
      }
      this.previewImage = file.url || file.preview;
      this.previewVisible = true;
    },

    // 自定义上传，可以自定义上传接口，不用通过action属性，还是非常灵活
    selfUploadFile(file) {
      // file 是上传的文件 其内容会在放在下面截图中
      // 后端需要接受的参数是 formData数据，
      const form = new FormData();
      form.append("file", file.file);
      // uploadFile 我自己的接口
      this.uploadFile(form).then((res) => {
        if (res.success) {
          // 调用组件内方法, 设置为成功状态
          file.onSuccess(res, file.file);
          file.status = "done";
        } else {
          file.onError();
          file.status = "error";
        }
      });
    },

    // 设置好头部
    uploadFile(parameter) {
      return axios({
        url: api.uploadFile,
        method: "post",
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: "localStorage.token",
        },
        data: parameter,
      });
    },
    handleChange({ fileList }) {
      this.fileList = fileList;
    },
    submitpost(e) {
      console.log("点击了发布按钮，内容是" + e);
    
      const that = this
      this.postForm.validateFields(async (err, values) => {
        if (!err) {
          console.log("接收到的表单数据 ", values);
          let form = new FormData();
          form.append("postTitle", values.postTitle);
          form.append("postMainText", values.postMainText);
         if(!that.fileList[0]){
          alert("请选择一张封面");
          }else{
         form.append("file", that.fileList[0].originFileObj);
          }      
           
          const rs = await postService.addPost(form);
          console.log(rs)
          this.$router.push({name:'TalkDetail'});
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
.total {
  margin: 0 auto;
min-width: 1000px;
  .header {
    display: flex;
    padding-left: 100px;
    padding-right: 100px;
    justify-content: space-between;
    align-content: center;
    height: 60px;
    background: white;
    //border: 2px solid black;
  }
  .header-left {
    display: flex;
    height: 60px;
    
    justify-content: center;
    align-items: center;
    //border: 2px solid blue;
    img{
      width:180px
    }
    span{
      color: rgb(9, 9, 9);
      font-weight: bold;

    }
  }
  .header-right {
    display: flex;
    
    align-items: center;
   
     //border: 2px solid red;
    
    
  }
  .content {
    padding-left: 100px;
    padding-right: 100px;
    padding-top: 20px;
    background: rgb(241, 239, 239);
    min-width: 1000px;

    display: flex;

    .content-left {
      flex: 1;
      background: white;
      margin-right: 15px;
      min-width: 80px;
    }
    .content-right {
      flex: 6;
      background: white;

     // border: 3px solid rgb(242, 219, 219);
    }
  }
  /* .formStyle{
     background:  white;

} */
  .title {
    font-size: 25px;
    color: balck;
    font-weight: bold;
  }
  .maintext {
    font-size: 16px;
    color: black;
    height: 400px;
  }
  .dividerStyle {
    background: rgba(102, 102, 160, 0.5);
  }

  .footer {
    background: white;
    height: 120px;
   // border: 2px solid #666;
  }
}
</style>