<template>
  <div>
    <a-button class="a" type="primary" @click="submitAddSentence(sentenceName)">
      上传句型
    </a-button>

    <div class="sentence-editor-top" >
         <div class="sentence-name" style="margin-top:20px">   
           <p style="font-size: 16px; color: #06afec;margin-bottom:10px"  >句型名称:</p>
          <a-radio-group
            @change="onSelectSentenceNameChange"
           v-model="sentenceName"
           defaultValue="赋值型"
          >
            <a-radio value="赋值型"> 赋值型 </a-radio>
            <a-radio value="命令型"> 命令型 </a-radio>
            <a-radio value="授权型"> 授权型 </a-radio>
            <a-radio value="消息型"> 消息型 </a-radio>
            <a-radio value="条件型"> 条件型 </a-radio>
            <a-radio value="循环型"> 循环型 </a-radio>
            <a-radio value="定时型"> 定时型 </a-radio>
            <a-radio value="监听型"> 监听型 </a-radio>
          </a-radio-group>
       </div>
        
          <div class="sentence-model" style="margin-top:10px">
            <p style="font-size: 16px; color: #06afec;margin-bottom:10px"  >句型模板:</p>
            <div v-if="this.sentenceName == '赋值型'" >
              <div class="sentence1">
            <span class="custom-input-span" style="margin-right: 10px"
              >设置</span
            >
           <a-tag  class="custom-input-tag" color="magenta" @click="tagChange('[变量名]')"> 变量名 </a-tag>
            <span class="custom-input-span" style="margin-right: 10px">为</span>
             <a-tag class="custom-input-tag" color="red" @click="tagChange('[常量或词汇]')"> 常量或词汇 </a-tag>
              </div>
          </div>
          <div v-else-if="this.sentenceName == '命令型'" > 
            <div class="sentence1">
          <a-tag class="custom-input-tag" color="red" @click="tagChange('[用户]')"> 用户 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">执行</span>
         <a-tag class="custom-input-tag" color="magenta" @click="tagChange('[工具]')"> 工具 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">的</span>
         <a-tag class="custom-input-tag" color="orange" @click="tagChange('[部件]')"> 部件 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >，其中</span
          >
         <a-tag class="custom-input-tag"  color="lime" @click="tagChange('[参数名]')"> 参数名 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">为</span>
          <a-tag class="custom-input-tag" color="blue" @click="tagChange('[参数值]')"> 参数值</a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >，结果为</span
          >
         <a-tag class="custom-input-tag" color="cyan" @click="tagChange('[术语]')"> 术语 </a-tag>
       </div>
       <div class="sentence2">
          <a-tag class="custom-input-tag" color="red" @click="tagChange('[用户]')"> 用户 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">在</span>
         <a-tag class="custom-input-tag" color="geekblue" @click="tagChange('[帮区]')"> 帮区 </a-tag>
         <span class="custom-input-span" style="margin-right: 10px">执行</span>
         <a-tag class="custom-input-tag" color="purple" @click="tagChange('[工具]')"> 工具 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">的</span>
         <a-tag class="custom-input-tag" color="volcano" @click="tagChange('[部件]')"> 部件 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >,</span
          >
         <a-tag class="custom-input-tag"  color="gold" @click="tagChange('[参数名]')"> 参数名 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">为</span>
          <a-tag class="custom-input-tag" color="red" @click="tagChange('[参数值]')"> 参数值</a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >，结果为</span
          >
         <a-tag class="custom-input-tag" color="lime" @click="tagChange('[术语]')"> 术语 </a-tag>
       </div>
       <div class="sentence3">
          <a-tag class="custom-input-tag" color="green" @click="tagChange('[用户]')"> 用户 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">执行</span>
        
         <a-tag class="custom-input-tag" color="blue" @click="tagChange('[部件]')"> 部件 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >(</span
          >
         <a-tag class="custom-input-tag"  color="cyan" @click="tagChange('[参数名]')"> 参数名 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">为</span>
          <a-tag class="custom-input-tag" color="purple" @click="tagChange('[参数值]')"> 参数值</a-tag>
          <span class="custom-input-span" style="margin-right: 10px">)</span >
       </div>
        </div>
        <div  v-else-if="this.sentenceName == '授权型'"> 
          <div class="sentence1">
         <a-tag class="custom-input-tag" color="volcano" @click="tagChange('[时间段]')"> 时间段 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >（允许或禁止）</span
          >
         <a-tag class="custom-input-tag" color="purple" @click="tagChange('[用户或岗位]')"> 用户或岗位 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">在</span>
          <a-tag class="custom-input-tag" color="green" @click="tagChange('[帮区]')"> 帮区</a-tag>
          <span class="custom-input-span" style="margin-right: 10px">执行</span>
         <a-tag class="custom-input-tag" color="gold" @click="tagChange('[工具]')"> 工具 </a-tag>
       </div>
        </div>
        <div v-else-if="this.sentenceName == '消息型'">
          <div class="sentence1">
          <span class="custom-input-span" style="margin-right: 10px"
            >发消息给</span
          >
          <a-tag class="custom-input-tag" color="gold" @click="tagChange('[消息板]')"> 消息板 </a-tag>

          <a-tag class="custom-input-tag" color="blue" @click="tagChange('[消息内容]')"> 消息内容 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >，配发</span
          >
           <a-tag class="custom-input-tag" color="cyan" @click="tagChange('[部件]')"> 部件 </a-tag>
          <span>,</span>
           <a-tag class="custom-input-tag"  color="red" @click="tagChange('[参数名]')"> 参数名 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">为</span>
          <a-tag class="custom-input-tag" color="purple" @click="tagChange('[参数值]')"> 参数值 </a-tag>
          </div>
        </div>
        <div v-else-if="this.sentenceName == '条件型'">
          <div class="snetence1">
          <span class="custom-input-span" style="margin-right: 10px"
            >如果</span
          >
          <a-tag  class="custom-input-tag" color="cyan" @click="tagChange('[术语]')"> 术语 </a-tag>

          <a-tag  class="custom-input-tag" color="red" @click="tagChange('[(判断符号)]')"> (判断符号) </a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >数值，则让</span
          >
           <a-tag  class="custom-input-tag" color="blue" @click="tagChange('[其他句型]')"> 其他句型</a-tag>
          </div>
          <div class="snetence2">
          <span class="custom-input-span" style="margin-right: 10px"
            >如果</span
          >
          <a-tag  class="custom-input-tag" color="red" @click="tagChange('[术语]')"> 术语 </a-tag>

          <a-tag  class="custom-input-tag" color="geekblue" @click="tagChange('[(空或不空)]')"> (空或不空) </a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >，则</span
          >
           <a-tag  class="custom-input-tag" color="cyan" @click="tagChange('[其他句型]')"> 其他句型</a-tag>
          </div>
           <div class="snetence3">
          <span class="custom-input-span" style="margin-right: 10px"
            >如果</span
          >
          <a-tag  class="custom-input-tag" color="lime" @click="tagChange('[术语]')"> 术语 </a-tag>

          <a-tag  class="custom-input-tag" color="orange" @click="tagChange('[(OK或nok)]')"> (ok或nok) </a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >，则</span
          >
           <a-tag  class="custom-input-tag" color="magenta" @click="tagChange('[其他句型]')"> 其他句型</a-tag>
          </div>
          <div class="snetence4">
          <span class="custom-input-span" style="margin-right: 10px"
            >如果</span
          >
          <a-tag  class="custom-input-tag" color="red" @click="tagChange('[术语]')"> 术语 </a-tag>

          <a-tag  class="custom-input-tag" color="lime" @click="tagChange('[(存在或不存在)]')"> (存在或不存在) </a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >，则</span
          >
           <a-tag  class="custom-input-tag"  color="gold" @click="tagChange('[其他句型]')"> 其他句型</a-tag>
          </div>
          <div class="snetence5">
          <span class="custom-input-span" style="margin-right: 10px"
            >如果</span
          >
          <a-tag  class="custom-input-tag" color="blue" @click="tagChange('[条件1]')"> 条件1 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px" >连接词</span>
          <a-tag  class="custom-input-tag" color="green" @click="tagChange('[条件2]')"> 条件2 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >，则</span
          >
           <a-tag  class="custom-input-tag"  color="cyan" @click="tagChange('[其他句型]')"> 其他句型</a-tag>
          </div>
          <div class="snetence6">
          <span class="custom-input-span" style="margin-right: 10px"
            >如果</span
          >
          <a-tag  class="custom-input-tag"  color="red" @click="tagChange('[术语]')"> 术语  </a-tag>
          <span class="custom-input-span" style="margin-right: 10px" >中</span>
          <a-tag  class="custom-input-tag" color="geekblue" @click="tagChange('字段')">字段</a-tag>
           <span class="custom-input-span" style="margin-right: 10px" >的</span>
           <a-tag  class="custom-input-tag" color="lime" @click="tagChange('(判断)')">(判断)</a-tag>
          <span class="custom-input-span" style="margin-right: 10px"
            >，则让</span
          >
           <a-tag  class="custom-input-tag"  color="volcano" @click="tagChange('[其他句型]')"> 其他句型</a-tag>
          </div>
        </div>
        <div  v-else-if="this.sentenceName == '循环型'" >
           <a-tag  color="red" @click="tagChange('[用户]')"> 用户 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">在</span>
           <a-tag  class="custom-input-tag" color="red" @click="tagChange('[帮区]')"> 帮区</a-tag>
          <span class="custom-input-span" style="margin-right: 10px">重复执行</span>
          <a-tag  class="custom-input-tag" color="blue" @click="tagChange('[工具]')"> 工具 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">的</span>
           <a-tag  class="custom-input-tag" color="green" @click="tagChange('[部件]')"> 部件 </a-tag>
           <a-tag  class="custom-input-tag" color="cyan" @click="tagChange('[数字]')"> 数字 </a-tag>
           <span class="custom-input-span" style="margin-right: 10px">次，</span>
           <a-tag  class="custom-input-tag" color="purple" @click="tagChange('[用户]')"> 参数名 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">为</span>
           <a-tag  class="custom-input-tag" color="gold" @click="tagChange('[帮区]')"> 参数值，</a-tag>
           <span class="custom-input-span" style="margin-right: 10px">结果为</span>
           <a-tag  class="custom-input-tag" color="magenta" @click="tagChange('[术语]')"> 术语</a-tag>
        </div>
        <div  v-else-if="this.sentenceName == '定时型'" >
          <div class="sentence1">
           <a-tag  class="custom-input-tag" color="red" @click="tagChange('[时间]')"> 时间 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">之后，</span>
           <a-tag  class="custom-input-tag" color="lime" @click="tagChange('[命令型或条件型]')"> 命令型或条件型 </a-tag>
          
           </div>
           <div class="sentence2">
            <span class="custom-input-span" style="margin-right: 10px">每</span>
           <a-tag  class="custom-input-tag" color="blue" @click="tagChange('[时间段]')"> 时间段 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">之后，</span>
           <a-tag  class="custom-input-tag" color="red" @click="tagChange('[命令型或条件型]')"> 命令型或条件型</a-tag>
         
           </div>
        </div>
          <div  v-else-if="this.sentenceName == '监听型'" >
          <div class="snetence1">
            <span class="custom-input-span" style="margin-right: 10px">当</span>
           <a-tag  class="custom-input-tag" color="geekblue" @click="tagChange('[用户]')"> 用户 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">在</span>
           <a-tag  class="custom-input-tag"  color="cyan" @click="tagChange('[帮区]')"> 帮区 </a-tag>
             <span class="custom-input-span" style="margin-right: 10px">执行</span>
           <a-tag  class="custom-input-tag" color="red" @click="tagChange('[工具]')"> 工具 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">的</span>
           <a-tag class="custom-input-tag"  color="gold" @click="tagChange('[部件]')"> 部件 </a-tag>
            <span class="custom-input-span" style="margin-right: 10px">，则</span>
           <a-tag  class="custom-input-tag" color="geekblue" @click="tagChange('[命令型]')"> 命令型 </a-tag>
           </div>
           <div class="snetence2">
            <span class="custom-input-span" style="margin-right: 10px">当</span>
           <a-tag  class="custom-input-tag" color="volcano" @click="tagChange('[工具部件]')"> 工具部件 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">产生</span>
           <a-tag  class="custom-input-tag" color="geekblue" @click="tagChange('[异常类型]')"> 异常类型</a-tag>
             <span class="custom-input-span" style="margin-right: 10px">,</span>
           <a-tag  class="custom-input-tag" color="volcano" @click="tagChange('[命令型或条件型]')">命令型或条件型</a-tag>
           </div>
            <div class="snetence3">
            <span class="custom-input-span" style="margin-right: 10px">当监听到中断,中断源为</span>
           <a-tag  class="custom-input-tag" color="magenta" @click="tagChange('[中断源]')"> 中断源 </a-tag>
          <span class="custom-input-span" style="margin-right: 10px">,中断类型为</span>
           <a-tag  class="custom-input-tag" color="red" @click="tagChange('[中断类型]')"> 中断类型</a-tag>
                <span class="custom-input-span" style="margin-right: 10px">,中断内容为</span>
           <a-tag  class="custom-input-tag"  color="lime" @click="tagChange('[中断类型]')"> 中断内容</a-tag>
             <span class="custom-input-span" style="margin-right: 10px">,</span>
           <a-tag  class="custom-input-tag" color="purple" @click="tagChange('[命令型]')">命令型</a-tag>
           </div>
        </div>
          </div>
          <div class="sentence-user-editor" style="margin-top:10px">
            <p style="font-size: 16px; color: #06afec;margin-bottom:10px">
              根据句型模板及所给标签自定义句型:
            </p>

            <div >
              <a-textarea
              ref="sentencTextArea"
      v-model="textAreaustomSentence"
      placeholder="你可以根据句型模板及里面的标签生产句型"
      :auto-size="{ minRows: 2, maxRows: 2 }"
    />      
            </div>
        
          </div>
        </div> 
         <a-divider />
 
    <div class="sentence-editor-bottom">
    
       <a-descriptions title="  刚刚所创建的句型详细信息" layout="vertical" bordered>
     <a-descriptions-item label="句型名称">
      {{ currentSentenceDetail.name}}
    </a-descriptions-item>
    <a-descriptions-item label="创建者">
    {{ currentSentenceDetail.creatorName}}
    </a-descriptions-item>
    <a-descriptions-item label="创建时间">
     {{ currentSentenceDetail.createTime}}
    </a-descriptions-item>

    <a-descriptions-item label="句型" :span="3">
      <a-badge status="processing" />{{ currentSentenceDetail.description}}
    </a-descriptions-item>
   
  </a-descriptions>
    </div>
  </div>
  
</template>

<script>
import SentenceService from "@/service/sentence_service";
const sentenceService = new SentenceService();
export default {
  data() {
    return {
      name: "SentencePage",
      components: {},
      sentenceName: "赋值型",
      textAreaustomSentence:'',
      pagination: {
        page: 1,
        count: 10,
        total: 0,
      },
      sentenceEntity:{
        description:'',
        name:'',
        type:'2',
      
      },
      currentSentenceDetail:{},
    };
  },
  beforeCreate() {
   
  },
  created() {},
  watch: {},
  computed: {},
  methods: {
    log(e) {
      console.log(e);
    },
    handleSubmit(a){
      console.log(a)
    },
    onSelectSentenceNameChange(e) {
      console.log("选中了", e.target.value);
      this.sentenceName= e.target.value;
      this.currentSentenceDetail='';

    },
     tagChange(tagValue) {
    
this.textAreaustomSentence+=tagValue;
console.log(this.textAreaustomSentence);
this.$refs.sentencTextArea.focus();

  
  },
    submitAddSentence(value) {
    
       this.sentenceEntity.description=this.textAreaustomSentence;
        this.sentenceEntity.name=value; 
        this.textAreaustomSentence='';
      sentenceService.createSentence(this.sentenceEntity,{
        onSuccess: (model, fModel, json) => {
            console.log(json);
            this.currentSentenceDetail=json.data.rows
            console.log(this.currentSentenceDetail);  
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },

      });
    },
  },
};
</script>
<style lang="scss" scoped>
.sentence-editor-top {
  min-width: 1000px;
  margin-left: 50px;
  margin-right: 50px;
 
 
  .custom-input {
    border: none; /*去除所有边框*/
    outline: none; /*去除选中时出现的边框*/
    border-bottom-color: rgb(194, 103, 103); /*下边框颜色*/
    border-bottom-left-radius: 0px; /*下左边框的圆角为0px*/
    border-bottom-right-radius: 0px; /*下右边框的圆角为0px*/
    border-bottom-width: 0.2px; /*下边框的宽度*/
    border-bottom-style: solid; /*下边框的样式 solid-->实线*/
    width: 80px;
    color: #1d81e2;
    font-size: 12px;
  }
  .custom-input-span {
    font-weight: bold;
    margin-bottom: 10px;
  }
  .custom-input-tag{
 margin-bottom: 10px;
  }
}
.sentence-editor-bottom {
  padding: 0 30px;
 
  border: 1px solid rgb(244, 243, 243);
  border-radius: 5px;
}
</style>>