<template>
  <q-dialog v-model="keywordCreate">
    <div>
      <div style="min-height: 100vh">
        <div class="q-pa-md">
          <br />
          <br />
          <form @submit.prevent="submitForm">
            <q-card>
              <q-card-section class="row">
                <div class="text-h6">키워드 등록</div>
                <q-space />
                <q-btn v-close-popup flat round dense icon="close"> </q-btn>
              </q-card-section>

              <!-- Radio Button -->
              <q-list class="row">
                <q-item tag="label" v-ripple>
                  <q-item-section avatar>
                    <q-radio
                      v-model="color"
                      val="normal-keyword"
                      color="blue"
                    ></q-radio>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label>일반 키워드 추가</q-item-label>
                    <q-item-label caption>With description </q-item-label>
                  </q-item-section>
                </q-item>

                <q-item tag="label" v-ripple>
                  <q-item-section avatar top>
                    <q-radio
                      v-model="color"
                      val="company-keyword"
                      color="blue"
                    ></q-radio>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label>기업 키워드 추가</q-item-label>
                    <q-item-label caption>With description </q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>

              <!-- 일반 키워드 일때-->
              <template v-if="color === 'normal-keyword'">
                <q-card-section>
                  <div class="q-mb-sm">
                    <q-input
                      outlined
                      v-model="keyword"
                      label="등록할 키워드"
                      placeholder="(ex. 블록체인)"
                      hint="한글, 영어 대소문자, 숫자 최대 20자"
                      :rules="[(val) => !!val || '필수값입니다.']"
                      ref="keyword"
                    ></q-input>

                    <q-card-section align="right">
                      <q-btn
                        label="등록"
                        color="primary"
                        @click="submitFormNormalKeyword()"
                      ></q-btn>
                    </q-card-section>
                  </div>
                </q-card-section>
              </template>

              <!-- 회사 키워드 일때 -->
              <template v-if="color === 'company-keyword'">
                <q-card-section>
                  <div class="q-mb-sm">
                    <q-input
                      outlined
                      v-model="companyName"
                      label="등록할 키워드"
                      placeholder="(ex. 삼성전자)"
                      hint="한글, 영어 대소문자, 숫자 최대 20자"
                      :rules="[(val) => !!val || '필수값입니다.']"
                      ref="companyName"
                    ></q-input>
                  </div>
                  <!-- :rules="[(val) => denySpacialChar(val) || '특수문자가 허용되지 않는 필수값입니다']" -->

                  <div class="q-mb-sm">
                    <q-input
                      outlined
                      v-model="companyCategory"
                      label="업종 분류"
                      placeholder="(ex. 통신 및 방송 장비 제조업)"
                      hint=" "
                      :rules="[
                        (val) =>
                          val.length <= 50 ||
                          'Please use maximum 50 characters',
                      ]"
                      ref="companyCategory"
                    ></q-input>
                  </div>

                  <div class="q-mb-sm">
                    <q-input
                      outlined
                      v-model="companyRepresentative"
                      label="대표자"
                      placeholder="(ex. 한종희, 경계현)"
                      hint=" "
                      :rules="[
                        (val) =>
                          val.length <= 50 ||
                          'Please use maximum 50 characters',
                      ]"
                      ref="companyRepresentative"
                    ></q-input>
                  </div>

                  <div class="q-mb-sm">
                    <q-input
                      outlined
                      v-model="companyLink"
                      label="회사 링크"
                      placeholder="(ex. https://www.samsung.com/sec/)"
                      hint=" "
                      :rules="[
                        (val) =>
                          val.length <= 500 ||
                          'Please use maximum 500 characters',
                      ]"
                      ref="companyLink"
                    ></q-input>
                  </div>
                </q-card-section>

                <q-card-section align="right">
                  <q-btn label="등록" color="primary" type="submit" />
                </q-card-section>
              </template>
            </q-card>
          </form>
        </div>
      </div>
    </div>
  </q-dialog>
</template>
<script>
import { ref, computed } from "vue";
import { createNormalKeywordApi } from "boot/keyword.js";
import { createCompanyKeywordApi } from "boot/keyword.js";
import { NormalKeywordReq } from "src/boot/request/KeywordReq";
import { CompanyKeywordReq } from "src/boot/request/KeywordReq";

export default {
  components: {},
  data() {
    return {
      sampleData: "",
      color: ref("normal-keyword"),
      keyword: "",
      companyName: "",
      companyCategory: "",
      companyRepresentative: "",
      companyLink: "",
      dense: ref(false),
    };
  },
  setup() {
    return {
      keywordCreate: ref(false),
    };
  },
  created() {},
  mounted() {},
  unmounted() {},

  methods: {
    submitFormNormalKeyword() {
      if (!(this.keyword === "")) {
        this.createNormalKeyword();
      } else alert("에러");
    },

    submitForm() {
      if (
        !this.$refs.companyName.hasError &
        !this.$refs.companyCategory.hasError &
        !this.$refs.companyRepresentative.hasError &
        !this.$refs.companyLink.hasError
      ) {
        this.createCompanyKeyword();
      } else alert("에러");
    },

    async createNormalKeyword() {
      await createNormalKeywordApi(
        new NormalKeywordReq(this.keyword),
        (response) => {},
        (error) => {}
      );
    },

    // 회사 키워드 일때 - API
    async createCompanyKeyword() {
      await createCompanyKeywordApi(
        new CompanyKeywordReq(
          this.companyName,
          this.companyCategory,
          this.companyRepresentative,
          this.companyLink
        ),
        (response) => {},
        (error) => {
          console.warn(
            error,
            CompanyKeywordReq,
            "createCompanyKeywordApi 실패"
          );
        }
      );
    },
  },

  watch: {
    keyword(val) {
      const regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/gi;
      if (regExp.exec(val) !== null || val.length > 20) {
        return (this.keyword = this.keyword.slice(0, -1));
      }
    },

    companyName(val) {
      const regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/gi;
      if (regExp.exec(val) !== null || val.length > 20) {
        return (this.companyName = this.companyName.slice(0, -1));
      }
    },
  },
};
</script>
