<template>
<!--   <div>这里可以创建帖子</div> -->
<div>
     <a-form :form="postForm" class="formStyle">
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
        </a-form>
</div>
</template>

<script>
export default {
  data() {
    return {
      previewVisible: false,
      previewImage: "",
      postForm: this.$form.createForm(this, { name: "coordinated" }),
      fileList: [],
    };
  },
  created() {},
  watch: {},
  computed: {},
  methods: {
    handleChange({ fileList }) {
      this.fileList = fileList;
    },
    submitpost(e) {
      console.log("点击了发布按钮，内容是" + e);

      const that = this;
      this.postForm.validateFields(async (err, values) => {
        if (!err) {
          console.log("接收到的表单数据 ", values);
          let form = new FormData();
          form.append("postTitle", values.postTitle);
          form.append("postMainText", values.postMainText);
          if (!that.fileList[0]) {
            alert("请选择一张封面");
          } else {
            form.append("file", that.fileList[0].originFileObj);
          }

          const rs = await postService.addPost(form);
          console.log(rs);
          this.$router.push({ name: "TalkDetail" });
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
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
</style>