import axios from 'axios'

const axiosInstance = axios.create({
  headers: {
    post: {        
      'Access-Control-Allow-Origin': '*'
    },
    common : {
      'Accepts' : 'application/json'
    }
  },
  withCredentials: true,
  baseURL: 'http://localhost:8079/api'
})

export default axiosInstance