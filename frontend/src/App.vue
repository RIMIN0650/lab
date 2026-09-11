<script setup>
import { ref } from 'vue'
import axios from 'axios'

const title = ref('')
const contents = ref('')

const createPost = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/post/new', {
      title: title.value,
      contents: contents.value,
    })

    console.log(response.data)
    console.log(response.data.id)
    console.log(response.status)
    // console.log(response.headers)

    alert('게시글이 등록되었습니다')

    title.value = ''
    contents.value = ''
  } catch (error) {
    console.error(error)
    alert('게시글 등록에 실패했습니다')
  }
}
</script>

<template>
  <div>
    <h1>게시글 작성</h1>

    <div>
      <label>제목</label>
      <input v-model="title" type="text" placeholder="제목을 입력하세요" />
    </div>

    <div>
      <label>내용</label>
      <textarea v-model="contents" placeholder="내용을 입력하세요"></textarea>
    </div>

    <button @click="createPost">등록</button>
  </div>
</template>

<style scoped></style>
