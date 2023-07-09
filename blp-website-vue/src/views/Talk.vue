<template>
  <div class="total">
    <div class="header">
      <img :src="blpLogo" @click="toHome"/>
       <span>论坛</span>
      <a-menu
        :default-selected-keys="['1']"
        mode="horizontal"
        @click="handleClick"
      >
        <a-sub-menu key="1">
          <span slot="title"><a-icon type="setting" /><span>剧本案例</span></span>
          <a-menu-item
            class="menuitemclass"
            v-for="item in businessList"
            :key="item.index"
          >
            {{ item.busnessType }}
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
       <span>词汇库</span>
        <span>句型库</span>
    </div>
    <div class="middleBusBck">
      <div class="textContent1">连接用户和功能</div>
      <div class="textContent2">
        用户根据自己提出的业务需求编写剧本即可执行逻辑,无需编写代码即可完成功能的开发。
      </div>
    </div>
    <div class="content">
      <div class="left"></div>
      <div class="right">
        <div class="rightContent">
          <a-tabs 
          default-active-key="1"
          :activeKey="tabActiveKey" 
          @change="callback">
            <a-tab-pane
              :tab="item.busnessType"
              v-for="item in businessList"
              :key="item.index"
            >

              <!-- {{ appcaseList }} 测试使用-->
              <a-list item-layout="horizontal" :data-source="appcaseList">
                <a-list-item
                  class="businessItem"
                  :style="index%2==0?whiteBckStyle:greyBckStyle"
                  slot="renderItem"
                  slot-scope="item, index"
                >
                  <div class="businessLeft">
                    <span class="businessLeftTitle">{{ item.name }}</span
                    ><br /><br />
                    <span class="businessLeftDesc">{{ item.description }}</span>
                  </div>
                  <div class="businessRight">
                    <!-- {{draftList }} 测试使用-->

                    <a-list
                      :grid="{
                        gutter: 16,
                        column: 4,
                        xs: 1,
                        sm: 2,
                        md: 4,
                        lg: 4,
                        xl: 6,
                        xxl: 3,
                      }"
                      :data-source="draftList"
                    >
                      <a-list-item
                        slot="renderItem"
                        slot-scope="item, index"
                        class="draftItem"
                        @click="draftClick(item.id)"
                      >
                        <div class="draftItemImg"><img :src="img()" /></div> 
                        <span class="draftItemTitle">{{ item.name }}</span
                        ><br /><br />
                      </a-list-item>
               
                    </a-list>
                  </div>
                </a-list-item>
              </a-list>
            </a-tab-pane>
          </a-tabs>
          <!-- <div></div> -->
        </div>
      </div>

    </div>
           <a-modal
           :dialog-style="{  'vertical-align':'center',width:'800px',height:'400px'}"
          
      :title="firstDraftChapter.title"
      :visible="draftChapterVisible"
      :footer="null"
      :maskStyle="chapterMaskStyle"
      @cancel="handleChapterCancel"
    >
      <p>{{ firstDraftChapter.content }}</p>
    </a-modal>
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


import blpLogo from "assets/blpLogo1.png";
import Img0 from "assets/randomImg/0.gif";
import Img1 from "assets/randomImg/1.gif";
import Img2 from "assets/randomImg/2.gif";
import Img3 from "assets/randomImg/3.gif";
import Img4 from "assets/randomImg/4.gif";
import Img5 from "assets/randomImg/5.gif";
import Img6 from "assets/randomImg/6.gif";
import Img7 from "assets/randomImg/7.gif";
import Img8 from "assets/randomImg/8.gif";
import Img9 from "assets/randomImg/9.gif";
//import businessbackgroud from "assets/business.png";

const applicationService = new ApplicationService();
const draftService = new DraftService();
const chapterService = new ChapterService();
export default {
  data() {
    const businessList = [
      { index: "1", busnessType: "物流业务" },
      { index: "2", busnessType: "服务操作系统业务" },
      { index: "3", busnessType: "管理型业务" },
      { index: "4", busnessType: "学习型业务" },
      { index: "5", busnessType: "计算型业务" },
      { index: "6", busnessType: "活动型业务" },
      { index: "7", busnessType: "其他业务" },
    ];
    //const Imgs=[Img0,Img1,Img2,Img3,Img4,Img5,Img6,Img7,Img8,Img9],
    return {
      blpLogo,
      // businessbackgroud,
      businessList,

      tabActiveKey: "1",
      appcaseList: [],
      draftList: [],
      str: "",
      Img0, Img1, Img2, Img3, Img4, Img5, Img6, Img7, Img8, Img9,
      whiteBckStyle:{'background':'white'},
      greyBckStyle:{'background': '#eaeaea'},
      
      firstDraftChapter:{},
      draftChapterVisible:false,
      
     
    };
  },
  created() {
    this.callback(1);
  },
  watch: {},
  computed: {},
    
  methods: {
    toHome() {
      this.$router.push({ name: "Home" });
    },
    img() {
        return eval('this.Img' + Math.round(Math.random() * 9))
    },
    handleClick(e) {
      this.tabActiveKey = e.key;
    },
    draftClick(id){
      //这个id 是剧本id，根据剧本id找该剧本下的章节list，但是只对第一条进行展示，以弹窗的形式展示
console.log(id);
this.draftChapterVisible=true;
chapterService.getDraftChapterListByDraftId({
draftId:id,
},{
   onSuccess: (model, fModel, json) => {
                    //console.log("根据剧本id找该剧本下的章节list");
                    //console.log(json);
                   this.firstDraftChapter = json.data.rows[0];
                    console.log("根据剧本id找该剧本下的章节list,只取第一条数据以弹窗的形式进行展示");
                   console.log(this.firstDraftChapter);
                  },
onFail: (msg, json) => {
                    console.log(msg, json);
                  },
})

    },
     
    handleChapterCancel(e) {
      console.log('点击了叉');
      this.draftChapterVisible = false;
    },
    callback(e) {
      this.tabActiveKey = e;
      console.log(e);
      applicationService.getAppcaseBybusTpyeId(
        { businessTypeId: e },
        {
          onSuccess: (model, fModel, json) => {
            console.log("根据业务id找应用案例");
            console.log(json);
            this.appcaseList = json.data.rows;
            console.log(this.appcaseList);
            json.data.rows.map((item) => {
              draftService.getDraftListByAppcaseId(
                { appcaseId: item.id },
                {
                  onSuccess: (model, fModel, json) => {
                    console.log("根据应用案例id找对应的所有剧本");
                    console.log(json);
                    this.draftList = json.data.rows;
                  },

                  onFail: (msg, json) => {
                    console.log(msg, json);
                  },
                }
              );
            });
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
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
  background: rgb(234, 225, 225);
  min-width: 915px;

  .header {
    display: flex;
    background: #fff;
    img {
      width: 16%;
    }
  }

  .middleBusBck {
    width: 100%;
    height: 340px;
    background: url("../assets/business.png") no-repeat;
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

    .left {
      // background: #e8e9ea;
      background: white;

      border-radius: 5px;
    }
    .right {
      width: 100%;
      background: #fff;
      border-radius: 5px;
      .rightBread {
        color: rgb(237, 170, 170);
        height: 30px;
        background: #eff3f6;
      }
      .rightContent {
        padding: 0 20px;

        border-radius: 5px;
        .businessItem {
          //border: 2px solid rgb(14, 14, 14);
          padding-top:50px;
          padding-bottom: 50px;
        }
        .businessLeft {
          width: 20%;
          //border: 2px solid rgb(237, 170, 170);
          .businessLeftTitle {
            color: black;
            font-size: 30px;
            font-style: oblique;
          }
          /* .businessLeftDesc{

          } */
        }
        .businessRight {
          width: 80%;
         // border: 2px solid rgb(171, 112, 112);
          margin-left: 40px;
          .draftItem {
           // border: 2px solid rgb(242, 13, 13);
            padding-top:5px;
            padding-bottom: 5px;
            text-align: center;
            .draftItemImg img {
              width: 100px;
              height: 100px;
            }
            
          }
          .draftItem:hover{
              background: rgb(245, 244, 244);
            }

        }
      }
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