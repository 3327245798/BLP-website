<template>
  <div class="wordpage-container" >
    <div class="wordku-container">
      <!-- 这个页面用来展示公共词汇 -->

      <div class="topbutton">
        <a-button class="a" type="primary" @click="toHome">
          点我回到首页
        </a-button>
         
      <!--   <a-button class="a" type="primary" @click="getCheckedWord">
          查询审核通过的词汇
        </a-button>
        <a-button class="a" type="primary" @click="getAllStatusWord">
          查询所有公共词汇
        </a-button> -->

        <a-input-group compact class="a" style="display:flex;justify-content:flex-start;width:340px">
      <a-select style="width: 40%" default-value="编辑者" v-model="searchType">
        <a-select-option value="编辑者" >
          编辑者
        </a-select-option>
        <a-select-option value="词汇状态" >
          词汇状态
        </a-select-option>
      </a-select>
     <a-input-search
          placeholder="根据条件进行搜索"
          enter-button
          v-model="searchName"
         
          @search="onSearch"
        >
        </a-input-search>
       
    </a-input-group>
       
        <a-button class="a" type="primary" @click="refresh">
          刷新
        </a-button>
      </div>

      <a-table
        bordered
        :rowKey="
          (record, index) => {
            return index;
          }
        "
        :loading="tableLoading"
        @expand="onExpand"
        @change="handleTableChange"
        :columns="columns"
        :data-source="wordData"
      >
        <!--  <p slot="expandedRowRender" slot-scope="text,record," style="margin: 0">
      {{ text }}
    </p> -->

        <a slot="name" slot-scope="text" style="font-size: 14px">{{ text }}</a>
        <span slot="customTitle"><a-icon type="smile-o" /> 名称</span>
        <span slot="customUpdateTimeTitle">
          修改时间
          <a-dropdown>
              <a-menu slot="overlay" @click="onDatePickClick" >
                
                <a-menu-item key="1"> 一个月内 </a-menu-item>
                <a-menu-item key="2"> 一周内</a-menu-item>
                <a-menu-item key="3"> 今天</a-menu-item>
              </a-menu>
              <a> <a-icon type="down" /> </a>
            </a-dropdown>
            </span>

        <span slot="creatorName" slot-scope="text">
          <a-tag style="font-size: 14px" color="purple">
            {{ text }}
          </a-tag>
        </span>
        <span slot="status" slot-scope="status">
          <a-tag
            style="font-size: 14px"
            v-if="status == '0'"
            :key="tag"
            color="red"
          >
            {{ "禁用" }}
          </a-tag>
          <a-tag
            style="font-size: 14px"
            v-else-if="status == '1'"
            :key="tag"
            color="orange"
          >
            {{ "审核中" }}
          </a-tag>
          <a-tag
            style="font-size: 14px"
            v-else-if="status == '2'"
            :key="tag"
            color="blue"
          >
            {{ "审核未通过" }}
          </a-tag>
          <a-tag style="font-size: 14px" v-else :key="tag" color="green">
            {{ "审核通过并启用" }}
          </a-tag>
        </span>

        <span slot="action" slot-scope="text, record">
          <a-button
            @click="openModifyWord(record)"
            type="primary"
            size="small"
            ghost
          >
            编辑
          </a-button>
          <a-divider type="vertical" />
          <a-popconfirm
            cancelText="取消"
            okText="确定"
            title="确认删除?"
            @confirm="deleteWord(record)"
          >
            <a-button type="danger" size="small" ghost> 删除 </a-button>
          </a-popconfirm>
        </span>
        <a-table
          :rowKey="
            (record, index) => {
              return index;
            }
          "
          slot="expandedRowRender"
          slot-scope="text"
          :columns="innerColumns"
          :data-source="innerWordVersionData"
          :pagination="false"
        >
         <!--  <span slot="status" slot-scope="text">
            <a-badge status="success" />Finished
          </span> -->
       <!--    <span slot="operation" slot-scope="text" class="table-operation">
            <a>Pause</a>
            <a>Stop</a>
            <a-dropdown>
              <a-menu slot="overlay">
                <a-menu-item> Action 1 </a-menu-item>
                <a-menu-item> Action 2 </a-menu-item>
              </a-menu>
              <a> More <a-icon type="down" /> </a>
            </a-dropdown>
          </span> -->
        </a-table>
      </a-table>
      
     
    </div>
    <div class="other-container"></div>
  </div>
</template>
<script>
import WordService from "@/service/word_service";
const wordService = new WordService();
const columns = [
  {
    //title: "名称",
    dataIndex: "name",
    key: "name",
    slots: { title: "customTitle" },
    scopedSlots: { customRender: "name" },
    align: "center",
    width: 65,
  },
  {
    title: "固件描述",
    dataIndex: "description",
    key: "description",
    align: "center",
    width: 100,
  },
  {
    title: "固件",
    dataIndex: "meaning",
    key: "meaning",
    align: "center",
    width: 290,
  

  },
  /* { title: "绑定固件名称",
    dataIndex: "unitName",
    key: "unitName",
    align: "center",
    width: 100,}, */
  {
    title: "编辑者",
    dataIndex: "creatorName",
    key: "creatorName",
    scopedSlots: { customRender: "creatorName" },
    align: "center",
    width: 40,
  },
  {
    //title: "修改时间",
    dataIndex: "updateTime",
    key: "updateTime",
    slots:{title:"customUpdateTimeTitle"},
    align: "center",
    width: 90,
  },
  {
    title: "状态",
    dataIndex: "status",
    key: "status",
    scopedSlots: { customRender: "status" },
    align: "center",
    width: 10,
    /* filters: [
      { text: "禁用", value: "0" },
      { text: "审核中", value: "1" },
      { text: "审核未通过", value: "2" },
      { text: "审核通过并启用", value: "3" },
    
    ],
    onFilter: (value, record) => record.status.indexOf(value) === 0, */
  },
];
const innerColumns = [
  { title: "版本号", dataIndex: "version", key: "version" },
  {
    title: "词汇名 ",
    dataIndex: "name",
    key: "name",
  },
 
  { title: "编辑者", dataIndex: "creatorName", key: "creatorName" },
   {
    title: "创建时间",
    dataIndex: "createTime",
    key: "createTime",
    //scopedSlots: { customRender: "status" },
  },
    {
    title: "词汇描述",
    dataIndex: "description",
    key: "description",
    scopedSlots: { customRender: "operation" },
  },
/*   {
    title: "操作",
    dataIndex: "operation",
    key: "operation",
    scopedSlots: { customRender: "operation" },
  }, */
];


export default {
  name: "WordPage",
  components: {},
  data() {
    return {
      wordData: [],
      columns,
      innerColumns,
      innerWordVersionData: [],
      searchName: "",
      searchType:'编辑者'
    };
  },
  computed: {},
  beforeCreate() {},
  created() {
   this.refresh();
  },
  watch: {},
  methods: {
    onChange(value) {
      console.log(value);
    },
  refresh(){
 wordService.getAllPublicWord({
      onSuccess: (model, fModel, json) => {
        console.log(json);
        this.wordData = json.data.rows.word;
     
      },
      onFail: (msg, json) => {
        console.log(msg, json);
      },
    });
  },
    onDatePickClick({key}){
  console.log(`点击了选项 ${key}`);
   console.log(`${key}`);
   let datePickValue=`${key}`;

console.log('datePickValue是'+datePickValue);
   if(datePickValue=='2'){
wordService.getAllWordsInWeek({
   onSuccess: (model, fModel, json) => {
          console.log(json); //一个周内创建的词汇
          this.wordData = json.data.rows.word;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },

});
   }
    else if(datePickValue=='1'){
wordService.getAllWordsInMonth({
   onSuccess: (model, fModel, json) => {
          console.log(json); //一月内创建的词汇
          this.wordData = json.data.rows.word;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },

});
   }
    else if(datePickValue=='3'){
    wordService.getAllWordsInDay({
   onSuccess: (model, fModel, json) => {
          console.log(json); //今天一天内创建的词汇
          this.wordData = json.data.rows.word;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },

});

   }
    },
    toHome() {
      this.$router.push({ name: "Home" });
    },
    getCheckedWord() {
      wordService.getCheckedWord({
        onSuccess: (model, fModel, json) => {
          console.log(json); //审核通过的公共词汇
          this.wordData = json.data.rows.word;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },
      });
    },
    getAllStatusWord() {
      wordService.getAllPublicWord({//查询所有词汇
        onSuccess: (model, fModel, json) => {
          console.log(json);
          this.wordData = json.data.rows.word;
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },
      });
    },
  
    onSearch(){

      if(this.searchType == '编辑者'){
        this.onSearchByCreatorName(this.searchName);
      }else {
        this.onSearchByStatus(this.searchName)
      }
    },
    onSearchByCreatorName(value) {
      console.log(value);
      if(value==""){this.refresh();}
      else{
   wordService.getPublicWordByCreatorName(
        {
          creatorName: value,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.wordData = json.data.rows.word;
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
      }
   
    },
     onSearchByStatus(value) {
      console.log(value);
      if(value==""){this.refresh();}
      else{
let status=0;
      if(value=="禁用"){
        status=0;
      }else if(value=="审核中"){
status=1;
      }else if(value=="审核未通过"){
status=2;
      }else if(value=="审核通过并启用"){
status=3;
      }
      wordService.getPublicWordByStatus(
        {
          status: status,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.wordData = json.data.rows.word;
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
      }
      
    },
    onExpand(expanded, record) {
      console.log(record);
      wordService.getAllWordVersionByWordId(
        {
          wordId: record.id,
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            this.innerWordVersionData = json.data.rows.wordVersion;
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
<style lang="scss">
.wordpage-container {
  height: 100%;
  display: flex;
  margin-left: 80px;
  margin-right: 120px;
  .wordku-container {
    flex: 1;
    position: relative;
    background: #fff;
    height: 100%;
    min-width: 1165px;
    padding-left: 30px;
    padding-top: 10px;
  }
  .topbutton {
    display: flex;
    //justify-content: space-between;
    .a {
      margin-right: 18px;
    }
  }
  /*   .ant-table {
    min-height: 352px !important;
  } */

 /*  .other-container {
    background: #f7f9fc;
    min-width: 30rem;
  } */

}
</style>