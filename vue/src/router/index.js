import VueRouter from "vue-router"
import Manage from "@/pages/Manage"
import About from "@/pages/About"
import User from "@/pages/User"
import Home from "@/pages/Home"
import store from "@/store";
import Login from "@/pages/Login";

const router = new VueRouter({
    routes: [
        {
            path: '/',
            name: 'Manage',
            component: Manage,
            redirect: '/home',
            children: [
                {
                    path: 'user',
                    name: 'User',
                    component: User,
                    meta: {pathName: '用户管理'}
                },
                {
                    path: 'home',
                    name: 'Home',
                    component: Home,
                    meta: {pathName: '首页'}
                }
            ]
        },
        {
            path: '/about',
            name: 'About',
            component: About
        },
        {
            path: '/login',
            name: 'Login',
            component: Login
        }
    ],
    mode: 'history'
})

router.beforeEach((to,from,next)=>{
    // if(to.path === from.path){
    //     return next(false)
    // }
    store.state.currentPath = to.meta.pathName
    next()
})

export default router