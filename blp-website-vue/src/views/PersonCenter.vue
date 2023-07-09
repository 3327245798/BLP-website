<template>
<!-- 这里是个人中心，可以设置自己的头像，个人中心封面等 -->
  <div class="total">
    <div class="header">
      <div class="header-left">
        <img :src="blpLogo" @click="toHome"/>
      <span class="a" @click="toHome">首页</span>
      <span class="a" >业务</span>
      </div>
      
        <div div class="header-right">
          <span class="a" >创作中心</span>
          <a-avatar slot="avatar" :src="touxiang1" alt="Han Solo" style="float:right;margin-right:20px" />
          </div>  
      
    </div>
   
    <div class="content"   style="display:relative">
      <div class="content-top">
        <div class="person-center-fengmian">
        <a-button class="person-center-fengmian-button"  ghost>
        上传个人中心封面
      </a-button>
        </div>
        <div  class="person-center-info">  <span style="margin-left:150px;margin-bottom:50px"> {{userName}}</span></div>
 <a-upload
      class="avatarAbsolute"
      list-type="picture-card"
      :file-list="fileList"
      @change="handleChange"
      @customRequest="selfUploadFile"
     
    >
      <div v-if="fileList.length < 1">
        <a-icon type="plus" />
        <div class="ant-upload-text">
          上传头像
        </div>
      </div>
    </a-upload>
      <a-modal :visible="previewVisible" :title="previewTitle"  @cancel="handleCancel">
       <img alt="example" style="width: 100%" :src="previewImage" />
</a-modal>

      </div>
     <div class="content-main">
      <div class="left">
<a-tabs default-active-key="1" @change="callback">
      <a-tab-pane key="1" tab="动态">
        动态
      </a-tab-pane>
      <a-tab-pane key="2" tab="帖子" force-render>
       帖子
      </a-tab-pane>
      <a-tab-pane key="3" tab=" 回答">
        回答
      </a-tab-pane>
    </a-tabs>

      </div>
      <div class="right">
        <a-avatar slot="avatar" :src="touxiang1" alt="Han Solo" />
        <span>创作</span>
        <a-button block  ghost style="margin-top:5px;border:1px solid grey;color:black">
        进入创作中心>>
      </a-button>

      </div>
</div>
    </div>
         
    <div class="footer">
      <span>020-62793083 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
      <span>xiaoxiao@scut.mail.edu.cn</span>
      <p>广州市华南理工大学B8实验室</p>
    </div>
  </div>
</template>

<script>
import ApplicationService from "@/service/application_service";
import DraftService from "@/service/draft_service";
import ChapterService from "@/service/chapter_service";
import UserInfoService from "@/service/userinfo_service";

import touxiang1 from "assets/touxiang1.jpg";
import blpLogo from "assets/blpLogo1.png";
const userInfoService = new UserInfoService();

const applicationService = new ApplicationService();
const draftService = new DraftService();
const chapterService = new ChapterService();
import { getUserName } from "@/common/env";


function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
  });
}
export default {
  data() {
    
    return {
      userName: getUserName(),
      blpLogo,    
      touxiang1,
      fileList:[],
       previewVisible: false,
      previewImage: '',
          
    };
  },
  created() {
    
  },
  watch: {},
  computed: {},
    
  methods: {
    toHome() {
      this.$router.push({ name: "Home" });
    },
     handleCancel() {
      this.previewVisible = false;
    },
   /*  async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj);
      }
      this.previewImage = file.url || file.preview;
      this.previewVisible = true;
    }, */
    handleChange({ fileList }) {
      this.fileList = fileList;
      console.log(this.fileList);
    },
     selfUploadFile(){
      // console.log(this.fileList[0]);
     }
  /*    async selfUploadFile(){
       let userInfoform = new FormData();
      userInfoform.append("file", this.fileList[0].originFileObj);

      let result=await userInfoService.updateUserTalkInfo(userInfoform);
      console.log(result);

    } */

   
  },
};
</script>

<style lang="scss" scoped>

.total {
  margin: 0 auto;
  background: rgb(234, 225, 225);
  min-width: 915px;

  .header {
    display: flex;
    background: #fff;
    height: 60px;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    img {
      width: 20%;
    }
    .header-left{
display: flex;
align-items: center;
margin-left: 220px;
span{
  font-weight: bold;
  margin-left: 20px;
}
    }
    .header-right{
      margin-right: 220px;
      display: flex;
      align-items: center;
      span{
  font-weight: bold;
  margin-right: 20px;
}
    }
  }
  .content {
   
    padding-left: 250px;
    padding-right: 250px;
  
    border-radius: 5px;
    .content-top{
background: #fff;
//border: 2px black solid;
border-radius: 1px;
margin-bottom: 5px;
height: 240px;
.person-center-fengmian{
 background:grey;
height: 120px; 
 //position: relative;
 .person-center-fengmian-button{
  margin-bottom: 50px;
  margin-right: 50px;
 }

}

.avatarAbsolute{

  position: absolution;
top:100px;
left: 50px;
}
    }
.content-main{
  display: flex;
  height: 600px;
    .left {
      flex: 3;
      // background: #e8e9ea;
      background: white;

      border-radius: 5px;
      margin-right: 15px;
    }
    .right {
       flex: 1;
     
      background: #fff;
      border-radius: 5px;
     
      
    }
    }
  }
  .footer {
    margin-top: 5px;
    background: white;
    height: 80px;
    width: 100%;
    font-size: 15px;
    padding-top: 10px;
    text-align: center;
    color: black;
  }
}
</style>