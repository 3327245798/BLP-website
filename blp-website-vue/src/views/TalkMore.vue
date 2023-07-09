
<template>
<!-- 这个页面是用来展示全部帖子的 -->
  <div class="total">
    <div class="header">
      <div class="header-left">
        <img :src="blpLogo" @click="$router.push({name:'Home'})" />
         
        <a-divider type="vertical" />
        <span >这是展示全部帖子</span>
      </div>
      <div class="header-right">
        <!-- <img />
        <span>头像</span> -->
         <a-avatar slot="avatar" :src="touxiang1" alt="Han Solo" />
      </div>
    </div>
    <div class="content">
      <div class="content-left">
       <a-list item-layout="vertical" size="large" :data-source="postListData">
          <a-list-item
            slot="renderItem"
            key="item.title"
            slot-scope="item, index"
            class="talklistitem"
          >
            <a-list-item-meta>
              <!--  <a-avatar slot="avatar" :src="item.avatar" /> -->
              <a slot="title" :class="talkListTitle" :href="item.href">{{
                item.title
              }}</a>
            </a-list-item-meta>
            <div class="talk-list">
              <div class="img_contnet" style="display:flex">
                <img
                  :src="item.img"
                  style="width: 180px; height: 100px; border: 2px solid block"
                />
                <div class="talk-list-content">{{ item.content }}</div>
              </div>
              <div style="padding: 1rem">
              <span style="margin-right: 8px">
                 <a-icon type="star-o" style="margin-right: 8px" />
                 {{starText}}
              </span>
              <span style="margin-right: 8px">
                 <a-icon type="like-o" style="margin-right: 8px" />
                 {{likeText}}
              </span>
              <span 
              style="margin-right: 8px"
              @click="handleCommentClick(item.id, item);"> 
              <a-icon type="message" style="margin-right: 8px" />
              {{ item.showComment ? '收起评论' : '展开评论'}}
              </span>
              </div>
            </div>

            <!--  点击评论图标，如果有人已发表评论，展示列表，如果没人发表评论，则“还没有评论，发表第一个评论吧” -->
            <div>
              <!-- 这个div与template同级 -->
              <div class="myComment" v-show="item.showComment">
                <a-avatar slot="avatar" :src="touxiang1" alt="Han Solo" />

                <div class="myComment-content">
                  <a-textarea
                    class="myCommentText"
                    :rows="1"
                    :value="myCommentValue"
                    @change="handleMyCommentChange"
                    placeholder="写下你的评论..."
                  />
                  <a-button
                    html-type="submit"
                    :loading="submittingMyComment"
                    type="primary"
                    @click="handleSubmitMyComment(item.id)"
                  >
                    发布
                  </a-button>
                </div>
              </div>
            </div>
            <div class="otherComment" v-show="item.showComment">
              <div class="otherCommentNav">
                <span>{{item.commentList.length+'条评论'}}</span>
                <span :style="{ float: 'right', 'margin-right': '50px' }">    
                  <input type="button" value="默认" />
                  <input type="button" value="最新" />
                </span>
              </div>
              <a-divider></a-divider>
              <div class="otherCommentContent" :style="{ display: 'flex' }">
                <a-list 
                item-layout="horizontal" 
                :data-source="item.commentList">
                  <a-list-item
                  class="postitemStyle"
                  slot="renderItem"
                  slot-scope="item, index">
                    <a-avatar slot="avatar" src="touxiang2" alt="Han Solo" />
                    <div>
                      <div
                        style=" font-size:15px;font-weight:bold;text-decoration:blink;color:black:width:100%"
                      >
                       {{item.commentorName}}
                      </div>
                      <div
                        style="
                          font-size: 15px;
                          color: black;
                          display: flex;
                          width: 80%;
                        "
                      >
                         {{item.content}}
                      </div>
                      <div
                        style="
                          font-size: 15px;
                          color: #808080;
                          
                        "
                      >
                        <span class="date-location" style="margin-right: 50px">
                         {{item.commentTime}} 
                        </span>
                        <span class="reply-like" >
                          <a-icon
                            type="message"
                            style="margin-right: 10px"
                          />回复
                          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                          <a-icon
                            type="like-o"
                            style="margin-right: 10px"
                          />点赞
                        </span>
                      </div>
                    </div>
                  </a-list-item>
                </a-list>
              </div>
              <!--  <div v-show="showReply">
                <a-textarea
                  :rows="1"
                  :value="replyComment"
                  @change="handleMyCommentChange"
                  placeholder="回复@王哲"
                />
                <a-button
                  html-type="submit"
                  :loading="submittingMyComment"
                  type="primary"
                  @click="handleSubmitMyComment(item.id)"
                >
                  发布
                </a-button>
              </div> -->
            </div>
          </a-list-item>
        </a-list>
      </div>
      <div class="content-right"></div>
    </div>
    <div class="footer"></div>
  </div>
</template>

<script>
import blpLogo from "assets/blpLogo1.png";
import touxiang1 from "assets/touxiang1.jpg";
import touxiang2 from "assets/touxiang2.png";
import ApplicationService from "@/service/application_service";
import PostService from "@/service/post_service";
import CommentService from "@/service/comment_service";
const applicationService = new ApplicationService();
const commentService = new CommentService();
const postService = new PostService();
import { getBaseUrl } from "@/common/env";
export default {
  data() {
    return {
      blpLogo,
      touxiang1,
      touxiang2,
      postListData:[],
      pagination: {
        onChange: (page) => {
          console.log(page);
        },
        pageSize: 3,
      },
       commentText:"1条评论", 
      starText:"456",
      likeText:"123",
       myCommentValue: "",
      submittingMyComment: false,
    
    
    };
  },
  created() {
    this.getAllPosts();
  },
  watch: {},
  computed: {},
  methods: {
      handleMyCommentChange(e) {
      this.myCommentValue = e.target.value;
    },
    async handleSubmitMyComment(id) {
      console.log(this.myCommentValue);
      if (!this.myCommentValue) {
        return;
      }

      this.submittingMyComment = true;
      //然后在处理一些异步请求，向后台提交数据，例子是setTimeOut
      const result = await commentService.addComment({
        postId: id,
        commentContent: this.myCommentValue,
      });
      console.log(result);
      if (result) {
        this.submittingMyComment = false;
        this.myCommentValue = "";
      }
    },
      /*获取所有的帖子 */
    async getAllPosts() {
      const response = await postService.getAllPosts();
      console.log("获取所有的帖子");
      console.log(response);

      let ListData = response.data.data.rows.post;
      this.postListData = ListData.map((item) => {
        item.img = getBaseUrl() + "&action=getImage&postId=" + item.id;
        item['showComment'] = false;
        item['commentList'] = [];
        return item;
      });
    },
      async handleCommentClick( postid, item) { 
          
      if (!item.showComment) {   
        let result=await commentService.getCommentByPostId(postid);
        item.commentList = result.data.data.rows.commentList
        
       console.log(result);
      } 
        item.showComment = !item.showComment      
    },  
    },
};
</script>

<style lang="scss" scoped>
.ant-list-item {
  background: white;
      border: 2px solid rgb(242, 219, 219);
      border-radius: 5px;
      margin-bottom: 5x;
      padding-bottom: 10px;
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
    align-items: center;
    img{
      width:180px
    }
    span{
      color: rgb(9, 9, 9);
      font-weight: bold;
    }
   
  }
  .header-right {
    span{
//text-align:center;
    vertical-align: center;
    }
    
  }
  .content {
    padding-left: 100px;
    padding-right: 100px;
    padding-top: 20px;
    background: rgb(241, 239, 239);

    display: flex;

    .content-left {
      flex: 2;
      .myComment {
    display: flex;
    padding-left: 10px;

    .myComment-content {
      display: flex;
      width: 100%;
      font-size: 12px;
      margin-left: 10px;
      margin-right: 10px;
      margin-bottom: 10px;
      .myCommentText {
        width: 80%;
      }
    }
  }
  .otherComment{
     padding-left: 10px;
  }
    }
    .content-right {
      flex: 0;
      background: white;

      border: 3px solid rgb(242, 219, 219);
    }
  }
  
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
    border: 2px solid #666;
  }
}
</style>