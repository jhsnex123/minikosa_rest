<!-- src/views/admin/AdminSuggestionView.vue -->
<template>
  <div class="container">
    <div id="viewPostWrapper">
      <div class="subtitle" v-if="adminStore.currentSuggestion">
        <!-- 게시글 제목 -->
        <h2>{{ adminStore.currentSuggestion.title }}</h2>
        <img src="/images/main_logo.png" alt="Main Logo" />
        <p class="description">사용자가 서비스 개선을 위해 제안한 내용입니다.</p>
      </div>

      <!-- 오류 메시지 표시 -->
      <div v-if="adminStore.errorMessage" class="description error">
        {{ adminStore.errorMessage }}
      </div>
      <!-- 성공 메시지 표시 -->
      <div v-if="adminStore.successMessage" class="description success">
        {{ adminStore.successMessage }}
      </div>

      <!-- 게시글 메타 정보 -->
      <div class="post-meta" v-if="adminStore.currentSuggestion">
        <span>가게명: {{ adminStore.currentSuggestion.storeName}}</span> |
        <span>작성자: {{ adminStore.currentSuggestion.memberName || 'Unknown' }}</span> |
        <span>작성일: {{ formatDate(adminStore.currentSuggestion.createdAt) }}</span> |
        <span>조회수: {{ adminStore.currentSuggestion.views }}</span>
      </div>

      <!-- 게시글 내용 -->
      <div class="post-content" v-if="adminStore.currentSuggestion">
        <p>{{ adminStore.currentSuggestion.content }}</p>
      </div>

      <!-- 버튼 컨테이너 -->
      <div class="button-container" v-if="adminStore.currentSuggestion">
        <!-- 맛집 추가 버튼 -->
        <button @click="navigateToCreateStore" class="add-button">맛집 추가</button>
        <!-- 사장 등록 버튼 -->
        <button @click="navigateToRegisterOwner" class="register-button">사장 등록</button>
        <!-- 글 삭제 버튼 -->
        <button @click="deleteSuggestion(adminStore.currentSuggestion.contactId)" class="delete-button">
          글 삭제
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted } from 'vue'
import { useAdminStore } from '../../stores/admin'
import { useRoute, useRouter } from 'vue-router'

export default {
  name: 'AdminSuggestionView',
  setup() {
    const adminStore = useAdminStore()
    const route = useRoute()
    const router = useRouter()

    const suggestionId = route.params.id
    console.log(`Suggestion ID from route: ${suggestionId}`) // 디버깅용 로그 추가

    onMounted(async () => {
      await adminStore.fetchSuggestion(suggestionId)
    })

    const deleteSuggestion = async (id) => {
      if (confirm('정말로 이 제안을 삭제하시겠습니까?')) {
        await adminStore.deleteSuggestion(id)
        // 삭제 후 목록 페이지로 이동
        router.push({ name: 'AdminSuggestionList' })
      }
    }

    const navigateToCreateStore = () => {
      router.push('/admin/create/store')
    }

    const navigateToRegisterOwner = () => {
      router.push('/admin/register/owner')
    }

    const formatDate = (dateString) => {
      const options = { year: 'numeric', month: '2-digit', day: '2-digit' }
      return new Date(dateString).toLocaleDateString('ko-KR', options)
    }

    return {
      adminStore,
      deleteSuggestion,
      navigateToCreateStore,
      navigateToRegisterOwner,
      formatDate
    }
  }
}
</script>

<style scoped>
/* 스타일은 기존 Thymeleaf 템플릿과 유사하게 유지 */
.container {
  max-width: 1200px;
  margin: 80px auto 0 auto;
  padding: 20px;
}

#viewPostWrapper {
  width: 800px;
  margin: 75px auto;
  background-color: #fff;
  text-align: center;
  padding: 30px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  margin-bottom: 100px;
}

#viewPostWrapper img {
  display: block;
  margin: 10px auto;
  width: 150px;
  height: auto;
}

#viewPostWrapper h2 {
  font-size: 28px;
  color: #ff885b;
  margin-bottom: 10px;
}

.post-meta {
  text-align: center;
  color: #666;
  margin-bottom: 30px;
}

.post-content {
  font-size: 16px;
  line-height: 1.8;
  margin-bottom: 40px;
  text-align: center;
}

.button-container {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.button-container .add-button,
.button-container .register-button,
.button-container .delete-button {
  background-color: #ff885b;
  border: none;
  color: white;
  padding: 12px 30px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  text-decoration: none;
  display: inline-block;
  text-align: center;
}

.button-container .delete-button {
  background-color: #e07a4d;
}

.button-container .add-button:hover,
.button-container .register-button:hover {
  background-color: #e07a4d;
}

.button-container .delete-button:hover {
  background-color: #c05a3f;
}

@media (max-width: 768px) {
  #viewPostWrapper {
    width: 90%;
    padding: 20px;
  }

  #viewPostWrapper h2 {
    font-size: 24px;
  }

  .button-container {
    flex-direction: column;
    gap: 10px;
  }

  .button-container .add-button,
  .button-container .register-button,
  .button-container .delete-button {
    width: 100%;
  }
}

@media (max-width: 480px) {
  #viewPostWrapper h2 {
    font-size: 20px;
  }

  .post-meta {
    font-size: 14px;
  }

  .button-container .add-button,
  .button-container .register-button,
  .button-container .delete-button {
    padding: 8px 16px;
    font-size: 14px;
  }
}

.error {
  color: red;
}

.success {
  color: green;
}
</style>
