<template>
<!-- 这里是首页，即论坛 -->
  <div class="total">
   <div class="header">
      <img :src="blpLogo"  style="cursor:pointer" @click="toHome" />
      <div class="header-right">
        <div>
          <span @click="$router.push('websiteIndex')" style="color:purple">论坛</span>
          <span @click="$router.push('businessTypeAppcase')">剧本案例</span>

          <span @click="$router.push('wordLib')">词汇库</span>
          <span @click="$router.push('sentenceLib')">句型库</span>
        </div>
        <div>
          <span @click="$router.push('talkWorkCenter')">创作中心</span>
          <a-avatar slot="avatar" :src="touxiang1" alt="Han Solo" />
        </div>
      </div>
    </div>
    <div class="middleBusBck">
      <div class="textContent1">自由讨论</div>
      <div class="textContent2">
        用户可以讨论帮语中的句型、词汇、剧本等，进行思想上的火花的碰撞。
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
            <a-list-item-meta class="talkListTitle">
              <!--  <a-avatar slot="avatar" :src="item.avatar" /> -->
              <a slot="title"  :href="item.href">{{
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
          
    <div class="footer">
      <span>020-62793083 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
      <span>xiaoxiao@scut.mail.edu.cn</span>
      <p>广州市华南理工大学B8实验室</p>
    </div>
  </div>
</template>

<script>

import blpLogo from "assets/blpLogo1.png";
import touxiang2 from "assets/touxiang2.png";
import touxiang1 from "assets/touxiang1.jpg";
import PostService from "@/service/post_service";
import CommentService from "@/service/comment_service";

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
    toHome() {
      this.$router.push({ name: "Home" });
    },
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
.ant-menu-horizontal > .ant-menu-submenu-selected {
  color: black;
  font-weight: bold;
  // border-bottom: 2px solid #eb2f96;
  border-bottom: none;
}
.ant-menu-horizontal > .ant-menu-submenu-selected:hover {
  color: black;
  font-weight: bold;
  // border-bottom: 2px solid #eb2f96;
  border-bottom: none;
}
.menuitemclass:hover {
  color: rgb(23, 22, 22);
  background: rgb(225, 225, 225);
}
.total {
  margin: 0 auto;
  background:rgb(249, 248, 248);
  min-width: 915px;
  .header {
    height: 60px;
    display: flex;
    background: #fff;
    //justify-content:space-between;
    align-items: center;
    padding: 0 100px;
    img {
      width: 16%;
    }
     .header-right{
      width:100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  
  span{
      margin-left: 60px;
      font-weight: bold;
      cursor: pointer;
    }
    span:hover{
       margin-left: 60px;
      font-weight: bold;
    color: purple;
    }
  }
  
   }

  .middleBusBck {
    width: 100%;
    height: 340px;
    background: url("../assets/talk.png") no-repeat;
    background-position: center;
    background-size: 100% 100%;
    opacity: 0.8;
    padding-top: 120px;
    padding-left: 20px;
    .textContent1 {
      color: black;
      font-size: 30px;
      font-style: oblique;
    }
    .textContent2 {
      color: black;
      margin-top: 10px;
    }
  }
  .content {
    display: flex;
   // border: 2px solid rgb(225, 225, 225);
    border-radius: 5px;
    margin:0 100px  ;
    border:2px solid  rgb(234, 225, 225) ;

    .left {
      // background: #e8e9ea;
      background: white;
      

      border-radius: 5px;
      .talkListTitle{
        font-size: 16px;
font-weight: bold;
      }
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
    .right {
      width: 100%;
      background: #fff;
    
      border-radius: 5px;
    
      }
    }
 
  .footer {
    // background: rgb(237, 170, 170);
    background: rgb(244, 242, 242);
    height: 120px;
    width: 100%;
    font-size: 15px;
    padding-top: 10px;
    text-align: center;
    color: black;
  }
}
</style>