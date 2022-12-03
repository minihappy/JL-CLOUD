import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()], // 配置需要使用的插件列表，这里将vue添加进去
  // 配置文件别名 vite1.0是/@/  2.0改为/@
  // 这里是将src目录配置别名为 /@ 方便在项目中导入src目录下的文件
  resolve: {
    // 配置路径别名
    alias: {
      '@': path.resolve(__dirname, '/src'),
    },
  },
  // 强制预构建插件包
  optimizeDeps: {
    include: ['axios'],
  },
  // 打包配置
  // build: {
  //   target: 'modules',
  //   outDir: 'dist', //指定输出路径
  //   assetsDir: 'assets', // 指定生成静态资源的存放路径
  //   minify: 'terser' // 混淆器，terser构建后文件体积更小
  // },
  // 本地运行配置，及反向代理配置
  server: {
    cors: true, // 默认启用并允许任何源
    host: '0.0.0.0', //ip地址
    port: 5173, //端口号
    open: true,//启动后是否自动打开浏览器
    //反向代理配置，注意rewrite写法，开始没看文档在这里踩了坑
    // proxy: {
    //   '/sys-comm': {
    //     target: 'http://127.0.0.1:8088',   //代理接口
    //     changeOrigin: true,
    //     rewrite: (path) => path.replace(/^\/sys-comm/, '')
    //   }
    // }
  }
})
