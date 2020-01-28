import axios from 'axios'

const axiosInstance = axios.create({
  headers: {
    post: {        // can be common or any other method
      'Access-Control-Allow-Origin': '*'
    }
  },
  baseURL: 'http://localhost:8079/api'
})

export default axiosInstance