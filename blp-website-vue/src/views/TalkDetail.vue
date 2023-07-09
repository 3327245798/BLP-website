<template>
  <div class="total">
    <div class="header">
      <div class="header-left">
        <img :src="blpLogo" @click="$router.push({name:'Home'})" />
        <span>这是发布帖子的地方</span>
      </div>
      <div class="header-right">
        <img />
        <span>头像</span>
      </div>
    </div>
    <div class="content">
      <div class="content-left">
        这里可以查看当前发布的帖子
      </div>
      <div class="content-right"></div>
    </div>
    <div class="footer"></div>
  </div>
</template>

<script>
import blpLogo from "assets/blpLogo1.png";
import axios from "axios";
import PostService from "@/service/post_service";
const postService = new PostService();
export default {
  data() {
    return {
      blpLogo,
     
      
    };
  },
  created() {},
  watch: {},
  computed: {},
  methods: {
   
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
  }
  .header-left {
    display: flex;
    height: 60px;
    justify-content: center;
    align-content: center;
  }
  .header-right {
    justify-content: center;
    align-content: center;
  }
  .content {
    height: 500px;
    padding-left: 100px;
    padding-right: 100px;
    padding-top: 20px;
    background: rgb(241, 239, 239);

    display: flex;

    .content-left {
      flex: 2;
    }
    .content-right {
      flex: 0;
      background: white;

      //border: 3px solid rgb(242, 219, 219);
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