package com.nuc.admin.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nuc.admin.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nuc.admin.manager.AdminManager;
import com.nuc.util.PaperUtil;
import com.soft.common.util.DateUtil;
import com.soft.common.util.JSONData;

@Controller
public class AdminAction {

    @Autowired
    AdminManager adminManager;

    public AdminManager getAdminManager() {
        return adminManager;
    }

    public void setAdminManager(AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    String tip;

    /**
     * @return String
     * @Title: saveAdmin
     * @Description: 保存修改个人信息
     */
    @RequestMapping(value = "admin/Admin_saveAdmin.action", method = RequestMethod.POST)
    public String saveAdmin(User paramsUser,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //验证用户会话是否失效
            if (!validateAdmin(httpSession)) {
                return "loginTip";
            }
            //保存修改个人信息
            adminManager.updateUser(paramsUser);
            //更新session
            User admin = new User();
            admin.setUser_id(paramsUser.getUser_id());
            admin = adminManager.queryUser(admin);
            httpSession.setAttribute("admin", admin);

            setSuccessTip("编辑成功", "modifyInfo.jsp", model);
        } catch (Exception e) {
            e.printStackTrace();
            setErrorTip("编辑异常", "modifyInfo.jsp", model);
        }
        return "infoTip";
    }

    /**
     * @return String
     * @Title: saveAdminPass
     * @Description: 保存修改个人密码
     */
    @RequestMapping(value = "admin/Admin_saveAdminPass.action", method = RequestMethod.POST)
    public String saveAdminPass(User paramsUser,
                                ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //验证用户会话是否失效
            if (!validateAdmin(httpSession)) {
                return "loginTip";
            }
            //保存修改个人密码
            adminManager.updateUser(paramsUser);
            //更新session
            User admin = (User) httpSession.getAttribute("admin");
            if (admin != null) {
                admin.setUser_pass(paramsUser.getUser_pass());
                httpSession.setAttribute("admin", admin);
            }

            setSuccessTip("修改成功", "modifyPwd.jsp", model);
        } catch (Exception e) {
            setErrorTip("修改异常", "modifyPwd.jsp", model);
        }
        return "infoTip";
    }


    /**
     * @return String
     * @Title: listUsers
     * @Description: 查询业主
     */
    @RequestMapping(value = "admin/Admin_listUsers.action")
    public String listUsers(User paramsUser, PaperUtil paperUtil,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            if (paramsUser == null) {
                paramsUser = new User();
            }
            if (paperUtil == null) {
                paperUtil = new PaperUtil();
            }
            //设置分页信息
            paperUtil.setPagination(paramsUser);
            //总的条数
            int[] sum = {0};
            //查询业主列表
            paramsUser.setUser_type(1);
            List<User> users = adminManager.listUsers(paramsUser, sum);
            model.addAttribute("users", users);
            model.addAttribute("paramsUser", paramsUser);
            paperUtil.setTotalCount(sum[0]);

            //查询房屋字典
            Room room = new Room();
            room.setStart(-1);
            List<Room> rooms = adminManager.listRooms(room, null);
            if (rooms == null) {
                rooms = new ArrayList<Room>();
            }
            model.addAttribute("rooms", rooms);

        } catch (Exception e) {
            setErrorTip("查询业主异常", "main.jsp", model);
            return "infoTip";
        }

        return "userShow";
    }

    /**
     * @return String
     * @Title: queryStudent
     * @Description: 查询业主
     */
    @RequestMapping(value = "Admin_queryStudent.action")
    @ResponseBody
    public JSONData queryStudent(User paramsUser,
                                 ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        JSONData jsonData = new JSONData();
        try {
            if (paramsUser == null) {
                paramsUser = new User();
            }
            paramsUser.setUser_type(1);
            paramsUser.setStart(-1);
            //查询业主列表
            List<User> users = adminManager.listUsers(paramsUser, null);

            jsonData.setResult("users", users);

        } catch (Exception e) {
            jsonData.setErrorReason("查询业主信息失败，服务器异常");
            return jsonData;
        }

        return jsonData;
    }

    /**
     * @return String
     * @Title: addUserShow
     * @Description: 显示添加业主页面
     */
    @RequestMapping(value = "admin/Admin_addUserShow.action", method = RequestMethod.GET)
    public String addUserShow(ModelMap model) {
        //查询房屋字典
        Room room = new Room();
        room.setStart(-1);
        List<Room> rooms = adminManager.listRooms(room, null);
        if (rooms == null) {
            rooms = new ArrayList<Room>();
        }
        model.addAttribute("rooms", rooms);

        Shops shops = new Shops();
        shops.setStart(-1);
        List<Shops> shopss = adminManager.listShopss(shops, null);
        if (shopss == null) {
            shopss = new ArrayList<Shops>();
        }
        model.addAttribute("shopss", shopss);

        return "userEdit";
    }

    /**
     * @return String
     * @Title: addUser
     * @Description: 添加业主
     */
    @RequestMapping(value = "admin/Admin_addUser.action", method = RequestMethod.POST)
    public String addUser(User paramsUser,
                          ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //检查业主是否存在
            User user = new User();
            user.setUser_name(paramsUser.getUser_name());
            user = adminManager.queryUser(user);
            if (user != null) {
                model.addAttribute("tip", "失败，该用户名已经存在！");
                model.addAttribute("user", paramsUser);
                //查询房屋字典
                Room room = new Room();
                room.setStart(-1);
                List<Room> rooms = adminManager.listRooms(room, null);
                if (rooms == null) {
                    rooms = new ArrayList<Room>();
                }
                model.addAttribute("rooms", rooms);

                Shops shops = new Shops();
                shops.setStart(-1);
                List<Shops> shopss = adminManager.listShopss(shops, null);
                if (shopss == null) {
                    shopss = new ArrayList<Shops>();
                }
                model.addAttribute("shopss", shopss);

                return "userEdit";
            }
            //添加业主
            paramsUser.setUser_type(1);
            paramsUser.setReg_date(DateUtil.getCurDateTime());
            adminManager.addUser(paramsUser);

            setSuccessTip("添加成功", "Admin_listUsers.action", model);
        } catch (Exception e) {
            setErrorTip("添加业主异常", "Admin_listUsers.action", model);
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: editUser
     * @Description: 编辑业主
     */
    @RequestMapping(value = "admin/Admin_editUser.action", method = RequestMethod.GET)
    public String editUser(User paramsUser,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //得到业主
            User user = adminManager.queryUser(paramsUser);
            model.addAttribute("user", user);

            //查询房屋字典
            Room room = new Room();
            room.setStart(-1);
            List<Room> rooms = adminManager.listRooms(room, null);
            if (rooms == null) {
                rooms = new ArrayList<Room>();
            }
            model.addAttribute("rooms", rooms);

            Shops shops = new Shops();
            shops.setStart(-1);
            List<Shops> shopss = adminManager.listShopss(shops, null);
            if (shopss == null) {
                shopss = new ArrayList<Shops>();
            }
            model.addAttribute("shopss", shopss);


        } catch (Exception e) {
            setErrorTip("查询业主异常", "Admin_listUsers.action", model);
            return "infoTip";
        }

        return "userEdit";
    }

    /**
     * @return String
     * @Title: saveUser
     * @Description: 保存编辑业主
     */
    @RequestMapping(value = "admin/Admin_saveUser.action", method = RequestMethod.POST)
    public String saveUser(User paramsUser,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //保存编辑业主
            adminManager.updateUser(paramsUser);

            setSuccessTip("编辑成功", "Admin_listUsers.action", model);
        } catch (Exception e) {
            setErrorTip("编辑业主异常", "Admin_listUsers.action", model);
            return "infoTip";
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: delUsers
     * @Description: 删除业主
     */
    @RequestMapping(value = "admin/Admin_delUsers.action")
    public String delUsers(User paramsUser,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //删除业主
            adminManager.delUsers(paramsUser);

            setSuccessTip("删除业主成功", "Admin_listUsers.action", model);
        } catch (Exception e) {
            setErrorTip("删除业主异常", "Admin_listUsers.action", model);
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: listWorkers
     * @Description: 查询员工
     */
    @RequestMapping(value = "admin/Admin_listWorkers.action")
    public String listWorkers(Worker paramsWorker, PaperUtil paperUtil,
                              ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            if (paramsWorker == null) {
                paramsWorker = new Worker();
            }
            //设置分页信息
            if (paperUtil == null) {
                paperUtil = new PaperUtil();
            }
            paperUtil.setPagination(paramsWorker);
            //总的条数
            int[] sum = {0};
            //查询员工列表
            List<Worker> workers = adminManager.listWorkers(paramsWorker, sum);
            model.addAttribute("workers", workers);
            paperUtil.setTotalCount(sum[0]);
            model.addAttribute("paramsWorker", paramsWorker);

            //员工类型
            model.addAttribute("worker_type", paramsWorker.getWorker_type());
            model.addAttribute("worker_typeDesc", paramsWorker.getWorker_typeDesc());

        } catch (Exception e) {
            setErrorTip("查询" + paramsWorker.getWorker_typeDesc() + "异常", "Admin_listWorkers.action?worker_type=" + paramsWorker.getWorker_type(), model);
            return "infoTip";
        }

        return "workerShow";
    }

    /**
     * @return String
     * @Title: queryWorker
     * @Description: 查询员工
     */
    @RequestMapping(value = "admin/Admin_queryWorker.action", method = RequestMethod.GET)
    public String queryWorker(Worker paramsWorker, ModelMap model) {
        try {
            //得到员工
            Worker worker = adminManager.queryWorker(paramsWorker);
            model.addAttribute("worker", worker);

            //员工类型
            model.addAttribute("worker_type", paramsWorker.getWorker_type());
            model.addAttribute("worker_typeDesc", paramsWorker.getWorker_typeDesc());

        } catch (Exception e) {
            setErrorTip("查询" + paramsWorker.getWorker_typeDesc() + "异常", "Admin_listWorkers.action?worker_type=" + paramsWorker.getWorker_type(), model);
            return "infoTip";
        }

        return "workerDetail";
    }

    /**
     * @return String
     * @Title: addWorkerShow
     * @Description: 显示添加员工页面
     */
    @RequestMapping(value = "admin/Admin_addWorkerShow.action")
    public String addWorkerShow(Worker paramsWorker, ModelMap model) {
        //员工类型
        model.addAttribute("worker_type", paramsWorker.getWorker_type());
        model.addAttribute("worker_typeDesc", paramsWorker.getWorker_typeDesc());

        return "workerEdit";
    }

    /**
     * @return String
     * @Title: addWorker
     * @Description: 添加员工
     */
    @RequestMapping(value = "admin/Admin_addWorker.action", method = RequestMethod.POST)
    public String addWorker(Worker paramsWorker,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //添加员工
            adminManager.addWorker(paramsWorker);

            setSuccessTip("发布成功", "Admin_listWorkers.action?worker_type=" + paramsWorker.getWorker_type(), model);
        } catch (Exception e) {
            setErrorTip("发布异常", "Admin_listWorkers.action?worker_type=" + paramsWorker.getWorker_type(), model);
            e.printStackTrace();
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: editWorker
     * @Description: 编辑员工
     */
    @RequestMapping(value = "admin/Admin_editWorker.action", method = RequestMethod.GET)
    public String editWorker(Worker paramsWorker,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //得到员工
            Worker worker = adminManager.queryWorker(paramsWorker);
            model.addAttribute("worker", worker);

            //员工类型
            model.addAttribute("worker_type", paramsWorker.getWorker_type());
            model.addAttribute("worker_typeDesc", paramsWorker.getWorker_typeDesc());

        } catch (Exception e) {
            setErrorTip("查询" + paramsWorker.getWorker_typeDesc() + "异常", "Admin_listWorkers.action?worker_type=" + paramsWorker.getWorker_type(), model);
            return "infoTip";
        }

        return "workerEdit";
    }

    /**
     * @return String
     * @Title: saveWorker
     * @Description: 保存编辑员工
     */
    @RequestMapping(value = "admin/Admin_saveWorker.action", method = RequestMethod.POST)
    public String saveWorker(Worker paramsWorker,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //保存编辑员工
            adminManager.updateWorker(paramsWorker);

            setSuccessTip("编辑成功", "Admin_listWorkers.action?worker_type=" + paramsWorker.getWorker_type(), model);
        } catch (Exception e) {
            tip = "编辑失败";
            model.addAttribute("worker", paramsWorker);

            //员工类型
            model.addAttribute("worker_type", paramsWorker.getWorker_type());
            model.addAttribute("worker_typeDesc", paramsWorker.getWorker_typeDesc());

            return "workerEdit";
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: delWorkers
     * @Description: 删除员工
     */
    @RequestMapping(value = "admin/Admin_delWorkers.action")
    public String delWorkers(Worker paramsWorker,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //删除员工
            adminManager.delWorkers(paramsWorker);

            setSuccessTip("删除" + paramsWorker.getWorker_typeDesc() + "成功", "Admin_listWorkers.action?worker_type=" + paramsWorker.getWorker_type(), model);
        } catch (Exception e) {
            setErrorTip("删除" + paramsWorker.getWorker_typeDesc() + "异常", "Admin_listWorkers.action?worker_type=" + paramsWorker.getWorker_type(), model);
        }

        return "infoTip";
    }
    //==================================================================================================
    /**
     * @return String
     * @Title: delShopss
     * @Description: 删除商铺
     */
    @RequestMapping(value = "admin/Admin_delShopss.action")
    public String delShopss(Shops paramsShops,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //删除房屋
            adminManager.delShopss(paramsShops);

            setSuccessTip("删除商铺成功", "Admin_listShopss.action", model);
        } catch (Exception e) {
            setErrorTip("删除商铺异常", "Admin_listShopss.action", model);
        }

        return "infoTip";
    }
    /**
     * @return String
     * @Title: listShopss
     * @Description: 查询商铺
     */
    @RequestMapping(value = "admin/Admin_listShopss.action")
    public String listShopss(Shops paramsShops, PaperUtil paperUtil,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            if (paramsShops == null) {
                paramsShops = new Shops();
            }
            if (paperUtil == null) {
                paperUtil = new PaperUtil();
            }
            //设置分页信息
            paperUtil.setPagination(paramsShops);
            //总的条数
            int[] sum = {0};
            //查询房屋列表
            //用户身份限制
            User admin = (User) httpSession.getAttribute("admin");
            if (admin.getUser_type() == 1) {
                paramsShops.setUser_id(admin.getUser_id());
            }
            List<Shops> shopss = adminManager.listShopss(paramsShops, sum);
            model.addAttribute("shopss", shopss);
            model.addAttribute("paramsShops", paramsShops);
            paperUtil.setTotalCount(sum[0]);

        } catch (Exception e) {
            setErrorTip("查询商铺异常", "main.jsp", model);
            return "infoTip";
        }

        return "shopsShow";
    }


    /**
     * @return String
     * @Title: addShopsShow
     * @Description: 显示添加商铺页面
     */
    @RequestMapping(value = "admin/Admin_addShopsShow.action", method = RequestMethod.GET)
    public String addShopsShow(ModelMap model) {
        return "shopsEdit";
    }

    /**
     * @return String
     * @Title: addShops
     * @Description: 添加商铺
     */
    @RequestMapping(value = "admin/Admin_addShops.action", method = RequestMethod.POST)
    public String addShops(Shops paramsShops,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //检查房屋是否存在
            Shops shops = new Shops();
            shops.setShops_no(paramsShops.getShops_no());
            shops = adminManager.queryShops(shops);
            if (shops != null) {
                model.addAttribute("tip", "失败，该商铺已经存在！");
                model.addAttribute("shops", paramsShops);
                return "shopsEdit";
            }
            //添加房屋
            adminManager.addShops(paramsShops);

            setSuccessTip("添加成功", "Admin_listShopss.action", model);
        } catch (Exception e) {
            e.printStackTrace();
            setErrorTip("添加商铺异常", "Admin_listShopss.action", model);
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: editRoom
     * @Description: 编辑商铺
     */
    @RequestMapping(value = "admin/Admin_editShops.action", method = RequestMethod.GET)
    public String editShops(Shops paramsShops,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //得到房屋
            Shops shops = adminManager.queryShops(paramsShops);

            System.out.println(shops);
            model.addAttribute("shops", shops);

        } catch (Exception e) {
            setErrorTip("查询商铺异常", "Admin_listShopss.action", model);
            return "infoTip";
        }

        return "shopsEdit";
    }

    /**
     * @return String
     * @Title: saveShops
     * @Description: 保存编辑商铺
     */
    @RequestMapping(value = "admin/Admin_saveShops.action", method = RequestMethod.POST)
    public String saveShops(Shops paramsShops,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //检查房屋是否存在
            Shops shops = new Shops();
            shops.setShops_no(paramsShops.getShops_no());
            shops = adminManager.queryShops(shops);
            if (shops != null && shops.getShops_id() != paramsShops.getShops_id()) {
                model.addAttribute("tip", "失败，该商铺名已经存在！");
                model.addAttribute("shops", paramsShops);
                return "shopsEdit";
            }

            //保存编辑商铺
            adminManager.updateShops(paramsShops);

            setSuccessTip("编辑成功", "Admin_listShopss.action", model);
        } catch (Exception e) {
            setErrorTip("编辑商铺异常", "Admin_listShopss.action", model);
            return "infoTip";
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: delRooms
     * @Description: 删除房屋
     */
    @RequestMapping(value = "admin/Admin_delRooms.action")
    public String delRooms(Room paramsRoom,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //删除房屋
            adminManager.delRooms(paramsRoom);

            setSuccessTip("删除房屋成功", "Admin_listRooms.action", model);
        } catch (Exception e) {
            setErrorTip("删除房屋异常", "Admin_listRooms.action", model);
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: listRooms
     * @Description: 查询房屋
     */
    @RequestMapping(value = "admin/Admin_listRooms.action")
    public String listRooms(Room paramsRoom, PaperUtil paperUtil,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            if (paramsRoom == null) {
                paramsRoom = new Room();
            }
            if (paperUtil == null) {
                paperUtil = new PaperUtil();
            }
            //设置分页信息
            paperUtil.setPagination(paramsRoom);
            //总的条数
            int[] sum = {0};
            //查询房屋列表
            //用户身份限制
            User admin = (User) httpSession.getAttribute("admin");
            if (admin.getUser_type() == 1) {
                paramsRoom.setUser_id(admin.getUser_id());
            }
            List<Room> rooms = adminManager.listRooms(paramsRoom, sum);
            model.addAttribute("rooms", rooms);
            model.addAttribute("paramsRoom", paramsRoom);
            paperUtil.setTotalCount(sum[0]);

        } catch (Exception e) {
            setErrorTip("查询房屋异常", "main.jsp", model);
            return "infoTip";
        }

        return "roomShow";
    }


    /**
     * @return String
     * @Title: addRoomShow
     * @Description: 显示添加房屋页面
     */
    @RequestMapping(value = "admin/Admin_addRoomShow.action", method = RequestMethod.GET)
    public String addRoomShow(ModelMap model) {
        return "roomEdit";
    }

    /**
     * @return String
     * @Title: addRoom
     * @Description: 添加房屋
     */
    @RequestMapping(value = "admin/Admin_addRoom.action", method = RequestMethod.POST)
    public String addRoom(Room paramsRoom,
                          ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //检查房屋是否存在
            Room room = new Room();
            room.setRoom_no(paramsRoom.getRoom_no());
            room = adminManager.queryRoom(room);
            if (room != null) {
                model.addAttribute("tip", "失败，该房屋已经存在！");
                model.addAttribute("room", paramsRoom);
                return "roomEdit";
            }
            //添加房屋
            adminManager.addRoom(paramsRoom);

            setSuccessTip("添加成功", "Admin_listRooms.action", model);
        } catch (Exception e) {
            e.printStackTrace();
            setErrorTip("添加房屋异常", "Admin_listRooms.action", model);
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: editRoom
     * @Description: 编辑房屋
     */
    @RequestMapping(value = "admin/Admin_editRoom.action", method = RequestMethod.GET)
    public String editRoom(Room paramsRoom,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //得到房屋
            Room room = adminManager.queryRoom(paramsRoom);
            model.addAttribute("room", room);

        } catch (Exception e) {
            setErrorTip("查询房屋异常", "Admin_listRooms.action", model);
            return "infoTip";
        }

        return "roomEdit";
    }

    /**
     * @return String
     * @Title: saveRoom
     * @Description: 保存编辑房屋
     */
    @RequestMapping(value = "admin/Admin_saveRoom.action", method = RequestMethod.POST)
    public String saveRoom(Room paramsRoom,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //检查房屋是否存在
            Room room = new Room();
            room.setRoom_no(paramsRoom.getRoom_no());
            room = adminManager.queryRoom(room);
            if (room != null && room.getRoom_id() != paramsRoom.getRoom_id()) {
                model.addAttribute("tip", "失败，该房屋名已经存在！");
                model.addAttribute("room", paramsRoom);
                return "roomEdit";
            }

            //保存编辑房屋
            adminManager.updateRoom(paramsRoom);

            setSuccessTip("编辑成功", "Admin_listRooms.action", model);
        } catch (Exception e) {
            setErrorTip("编辑房屋异常", "Admin_listRooms.action", model);
            return "infoTip";
        }

        return "infoTip";
    }


//==================================================================================================


    /**
     * @return String
     * @Title: listCars
     * @Description: 查询车位
     */
    @RequestMapping(value = "admin/Admin_listCars.action")
    public String listCars(Car paramsCar, PaperUtil paperUtil,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            if (paramsCar == null) {
                paramsCar = new Car();
            }
            //设置分页信息
            if (paperUtil == null) {
                paperUtil = new PaperUtil();
            }
            paperUtil.setPagination(paramsCar);
            //总的条数
            int[] sum = {0};
            //查询车位列表
            List<Car> cars = adminManager.listCars(paramsCar, sum);
            model.addAttribute("cars", cars);
            paperUtil.setTotalCount(sum[0]);
            model.addAttribute("paramsCar", paramsCar);

        } catch (Exception e) {
            setErrorTip("查询车位信息异常", "Admin_listCars.action", model);
            return "infoTip";
        }

        return "carShow";
    }

    /**
     * @return String
     * @Title: queryCar
     * @Description: 查询车位
     */
    @RequestMapping(value = "admin/Admin_queryCar.action", method = RequestMethod.GET)
    public String queryCar(Car paramsCar, ModelMap model) {
        try {
            //得到车位
            Car car = adminManager.queryCar(paramsCar);
            model.addAttribute("car", car);

        } catch (Exception e) {
            setErrorTip("查询车位信息异常", "Admin_listCars.action", model);
            return "infoTip";
        }

        return "carDetail";
    }

    /**
     * @return String
     * @Title: addCarShow
     * @Description: 显示添加车位页面
     */
    @RequestMapping(value = "admin/Admin_addCarShow.action")
    public String addCarShow(Car paramsCar, ModelMap model) {
        return "carEdit";
    }

    /**
     * @return String
     * @Title: addCar
     * @Description: 添加车位
     */
    @RequestMapping(value = "admin/Admin_addCar.action", method = RequestMethod.POST)
    public String addCar(Car paramsCar,
                         ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //添加车位
            adminManager.addCar(paramsCar);

            setSuccessTip("发布成功", "Admin_listCars.action", model);
        } catch (Exception e) {
            setErrorTip("发布异常", "Admin_listCars.action", model);
            e.printStackTrace();
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: editCar
     * @Description: 编辑车位
     */
    @RequestMapping(value = "admin/Admin_editCar.action", method = RequestMethod.GET)
    public String editCar(Car paramsCar,
                          ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //得到车位
            Car car = adminManager.queryCar(paramsCar);
            model.addAttribute("car", car);

        } catch (Exception e) {
            setErrorTip("查询车位信息异常", "Admin_listCars.action", model);
            return "infoTip";
        }

        return "carEdit";
    }

    /**
     * @return String
     * @Title: saveCar
     * @Description: 保存编辑车位
     */
    @RequestMapping(value = "admin/Admin_saveCar.action", method = RequestMethod.POST)
    public String saveCar(Car paramsCar,
                          ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //保存编辑车位
            adminManager.updateCar(paramsCar);

            setSuccessTip("编辑成功", "Admin_listCars.action", model);
        } catch (Exception e) {
            tip = "编辑失败";
            model.addAttribute("car", paramsCar);
            return "carEdit";
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: delCars
     * @Description: 删除车位
     */
    @RequestMapping(value = "admin/Admin_delCars.action")
    public String delCars(Car paramsCar,
                          ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //删除车位
            adminManager.delCars(paramsCar);

            setSuccessTip("删除车位信息成功", "Admin_listCars.action", model);
        } catch (Exception e) {
            setErrorTip("删除车位信息异常", "Admin_listCars.action", model);
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: listFacis
     * @Description: 查询公共设施信息
     */
    @RequestMapping(value = "admin/Admin_listFacis.action")
    public String listFacis(Faci paramsFaci, PaperUtil paperUtil,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            if (paramsFaci == null) {
                paramsFaci = new Faci();
            }
            //设置分页信息
            paperUtil.setPagination(paramsFaci);
            //总的条数
            int[] sum = {0};
            //查询公共设施信息列表
            List<Faci> facis = adminManager.listFacis(paramsFaci, sum);
            model.addAttribute("facis", facis);
            paperUtil.setTotalCount(sum[0]);
            model.addAttribute("paramsFaci", paramsFaci);

        } catch (Exception e) {
            setErrorTip("查询公共设施异常", "Admin_listFacis.action", model);
            return "infoTip";
        }

        return "faciShow";
    }

    /**
     * @return String
     * @Title: queryFaci
     * @Description: 查询公共设施信息
     */
    @RequestMapping(value = "admin/Admin_queryFaci.action", method = RequestMethod.GET)
    public String queryFaci(Faci paramsFaci,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //得到公共设施信息
            Faci faci = adminManager.queryFaci(paramsFaci);
            model.addAttribute("faci", faci);

        } catch (Exception e) {
            setErrorTip("查询公共设施异常", "Admin_listFacis.action", model);
            return "infoTip";
        }

        return "faciDetail";
    }

    /**
     * @return String
     * @Title: addFaciShow
     * @Description: 显示添加公共设施信息页面
     */
    @RequestMapping(value = "admin/Admin_addFaciShow.action")
    public String addFaciShow(Faci paramsFaci,
                              ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {

        return "faciEdit";
    }

    /**
     * @return String
     * @Title: addFaci
     * @Description: 添加公共设施信息
     */
    @RequestMapping(value = "admin/Admin_addFaci.action")
    public String addFaci(Faci paramsFaci,
                          ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //添加公共设施信息
            adminManager.addFaci(paramsFaci);

            setSuccessTip("添加成功", "Admin_listFacis.action", model);
        } catch (Exception e) {
            setErrorTip("添加异常", "Admin_listFacis.action", model);
            e.printStackTrace();
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: editFaci
     * @Description: 编辑公共设施信息
     */
    @RequestMapping(value = "admin/Admin_editFaci.action")
    public String editFaci(Faci paramsFaci,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //得到公共设施信息
            Faci faci = adminManager.queryFaci(paramsFaci);
            model.addAttribute("faci", faci);

        } catch (Exception e) {
            setErrorTip("查询公共设施异常", "Admin_listFacis.action", model);
            return "infoTip";
        }

        return "faciEdit";
    }

    /**
     * @return String
     * @Title: saveFaci
     * @Description: 保存编辑公共设施信息
     */
    @RequestMapping(value = "admin/Admin_saveFaci.action")
    public String saveFaci(Faci paramsFaci,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //保存编辑公共设施信息
            adminManager.updateFaci(paramsFaci);

            setSuccessTip("编辑成功", "Admin_listFacis.action", model);
        } catch (Exception e) {
            tip = "编辑失败";
            model.addAttribute("faci", paramsFaci);

            return "faciEdit";
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: delFacis
     * @Description: 删除公共设施信息
     */
    @RequestMapping(value = "admin/Admin_delFacis.action")
    public String delFacis(Faci paramsFaci,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //删除公共设施信息
            adminManager.delFacis(paramsFaci);

            setSuccessTip("删除公共设施成功", "Admin_listFacis.action", model);
        } catch (Exception e) {
            setErrorTip("删除公共设施异常", "Admin_listFacis.action", model);
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: listWyfees
     * @Description: 查询物业缴费信息
     */
    @RequestMapping(value = "admin/Admin_listWyfees.action")
    public String listWyfees(Wyfee paramsWyfee, PaperUtil paperUtil,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            if (paramsWyfee == null) {
                paramsWyfee = new Wyfee();
            }
            //设置分页信息
            paperUtil.setPagination(paramsWyfee);
            //总的条数
            int[] sum = {0};
            //用户身份限制
            User admin = (User) httpSession.getAttribute("admin");
            if (admin.getUser_type() == 1) {
                paramsWyfee.setUser_id(admin.getUser_id());
            }
            //查询物业缴费信息列表
            List<Wyfee> wyfees = adminManager.listWyfees(paramsWyfee, sum);
            model.addAttribute("wyfees", wyfees);
            paperUtil.setTotalCount(sum[0]);
            model.addAttribute("paramsWyfee", paramsWyfee);

            //查询房屋字典
            Room room = new Room();
            room.setStart(-1);
            List<Room> rooms = adminManager.listRooms(room, null);
            if (rooms == null) {
                rooms = new ArrayList<Room>();
            }
            model.addAttribute("rooms", rooms);

        } catch (Exception e) {
            e.printStackTrace();
            setErrorTip("查询物业缴费信息异常", "main.jsp", model);
            return "infoTip";
        }

        return "wyfeeShow";
    }

    /**
     * @return String
     * @Title: addWyfeeShow
     * @Description: 显示添加物业缴费信息页面
     */
    @RequestMapping(value = "admin/Admin_addWyfeeShow.action")
    public String addWyfeeShow(Wyfee paramsWyfee,
                               ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        //查询房屋字典
        Room room = new Room();
        room.setStart(-1);
        List<Room> rooms = adminManager.listRooms(room, null);
        if (rooms == null) {
            rooms = new ArrayList<Room>();
        }
        model.addAttribute("rooms", rooms);

        return "wyfeeEdit";
    }

    /**
     * @return String
     * @Title: addWyfee
     * @Description: 添加物业缴费信息
     */
    @RequestMapping(value = "admin/Admin_addWyfee.action")
    public String addWyfee(Wyfee paramsWyfee,
                           ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //添加物业缴费信息
            adminManager.addWyfee(paramsWyfee);

            setSuccessTip("提交成功", "Admin_listWyfees.action", model);
        } catch (Exception e) {
            e.printStackTrace();
            setErrorTip("提交物业缴费信息异常", "Admin_listWyfees.action", model);
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: editWyfee
     * @Description: 编辑物业缴费信息
     */
    @RequestMapping(value = "admin/Admin_editWyfee.action")
    public String editWyfee(Wyfee paramsWyfee,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //得到物业缴费信息
            Wyfee wyfee = adminManager.queryWyfee(paramsWyfee);
            model.addAttribute("wyfee", wyfee);

            //查询房屋字典
            Room room = new Room();
            room.setStart(-1);
            List<Room> rooms = adminManager.listRooms(room, null);
            if (rooms == null) {
                rooms = new ArrayList<Room>();
            }
            model.addAttribute("rooms", rooms);

        } catch (Exception e) {
            setErrorTip("查询物业缴费信息异常", "Admin_listWyfees.action", model);
            return "infoTip";
        }

        return "wyfeeEdit";
    }

    /**
     * @return String
     * @Title: saveWyfee
     * @Description: 保存编辑物业缴费信息
     */
    @RequestMapping(value = "admin/Admin_saveWyfee.action")
    public String saveWyfee(Wyfee paramsWyfee,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //保存编辑物业缴费信息
            adminManager.updateWyfee(paramsWyfee);

            setSuccessTip("编辑成功", "Admin_listWyfees.action", model);
        } catch (Exception e) {
            setErrorTip("编辑失败", "Admin_listWyfees.action", model);
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: delWyfees
     * @Description: 删除物业缴费信息
     */
    @RequestMapping(value = "admin/Admin_delWyfees.action")
    public String delWyfees(Wyfee paramsWyfee,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //删除物业缴费信息
            adminManager.delWyfees(paramsWyfee);

            setSuccessTip("删除物业缴费信息成功", "Admin_listWyfees.action", model);
        } catch (Exception e) {
            setErrorTip("删除物业缴费信息异常", "Admin_listWyfees.action", model);
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: listCarfees
     * @Description: 查询车位缴费信息
     */
    @RequestMapping(value = "admin/Admin_listCarfees.action")
    public String listCarfees(Carfee paramsCarfee, PaperUtil paperUtil,
                              ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            if (paramsCarfee == null) {
                paramsCarfee = new Carfee();
            }
            //设置分页信息
            paperUtil.setPagination(paramsCarfee);
            //总的条数
            int[] sum = {0};
            //用户身份限制
            User admin = (User) httpSession.getAttribute("admin");
            if (admin.getUser_type() == 1) {
                paramsCarfee.setUser_id(admin.getUser_id());
            }
            //查询车位缴费信息列表
            List<Carfee> carfees = adminManager.listCarfees(paramsCarfee, sum);
            model.addAttribute("carfees", carfees);
            paperUtil.setTotalCount(sum[0]);
            model.addAttribute("paramsCarfee", paramsCarfee);

        } catch (Exception e) {
            setErrorTip("查询车位缴费信息异常", "main.jsp", model);
            return "infoTip";
        }

        return "carfeeShow";
    }

    /**
     * @return String
     * @Title: addCarfeeShow
     * @Description: 显示添加车位缴费信息页面
     */
    @RequestMapping(value = "admin/Admin_addCarfeeShow.action")
    public String addCarfeeShow(Carfee paramsCarfee,
                                ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        //查询业主字典
        User user = new User();
        user.setUser_type(1);
        user.setStart(-1);
        List<User> users = adminManager.listUsers(user, null);
        if (users == null) {
            users = new ArrayList<User>();
        }
        model.addAttribute("users", users);

        return "carfeeEdit";
    }

    /**
     * @return String
     * @Title: addCarfee
     * @Description: 添加车位缴费信息
     */
    @RequestMapping(value = "admin/Admin_addCarfee.action")
    public String addCarfee(Carfee paramsCarfee,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //添加车位缴费信息
            adminManager.addCarfee(paramsCarfee);

            setSuccessTip("提交成功", "Admin_listCarfees.action", model);
        } catch (Exception e) {
            e.printStackTrace();
            setErrorTip("提交车位缴费信息异常", "Admin_listCarfees.action", model);
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: editCarfee
     * @Description: 编辑车位缴费信息
     */
    @RequestMapping(value = "admin/Admin_editCarfee.action")
    public String editCarfee(Carfee paramsCarfee,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //得到车位缴费信息
            Carfee carfee = adminManager.queryCarfee(paramsCarfee);
            model.addAttribute("carfee", carfee);

            //查询业主字典
            User user = new User();
            user.setStart(-1);
            user.setUser_type(1);
            List<User> users = adminManager.listUsers(user, null);
            if (users == null) {
                users = new ArrayList<User>();
            }
            model.addAttribute("users", users);

        } catch (Exception e) {
            setErrorTip("查询车位缴费信息异常", "Admin_listCarfees.action", model);
            return "infoTip";
        }

        return "carfeeEdit";
    }

    /**
     * @return String
     * @Title: saveCarfee
     * @Description: 保存编辑车位缴费信息
     */
    @RequestMapping(value = "admin/Admin_saveCarfee.action")
    public String saveCarfee(Carfee paramsCarfee,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //保存编辑车位缴费信息
            adminManager.updateCarfee(paramsCarfee);

            setSuccessTip("编辑成功", "Admin_listCarfees.action", model);
        } catch (Exception e) {
            setErrorTip("编辑失败", "Admin_listCarfees.action", model);
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: delCarfees
     * @Description: 删除车位缴费信息
     */
    @RequestMapping(value = "admin/Admin_delCarfees.action")
    public String delCarfees(Carfee paramsCarfee,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //删除车位缴费信息
            adminManager.delCarfees(paramsCarfee);

            setSuccessTip("删除车位缴费信息成功", "Admin_listCarfees.action", model);
        } catch (Exception e) {
            setErrorTip("删除车位缴费信息异常", "Admin_listCarfees.action", model);
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: listRepairlists
     * @Description: 查询报修
     */
    @RequestMapping(value = "admin/Admin_listRepairlists.action")
    public String listRepairlists(Repairlist paramsRepairlist, PaperUtil paperUtil,
                                  ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            if (paramsRepairlist == null) {
                paramsRepairlist = new Repairlist();
            }
            //设置分页信息
            paperUtil.setPagination(paramsRepairlist);
            //总的条数
            int[] sum = {0};
            //用户身份限制
            User admin = (User) httpSession.getAttribute("admin");
            if (admin.getUser_type() == 1) {
                paramsRepairlist.setUser_id(admin.getUser_id());
            }
            //查询报修列表
            List<Repairlist> repairlists = adminManager.listRepairlists(paramsRepairlist, sum);
            model.addAttribute("repairlists", repairlists);
            paperUtil.setTotalCount(sum[0]);
            model.addAttribute("paramsRepairlist", paramsRepairlist);

            //查询房屋字典
            Room room = new Room();
            room.setStart(-1);
            List<Room> rooms = adminManager.listRooms(room, null);
            if (rooms == null) {
                rooms = new ArrayList<Room>();
            }
            model.addAttribute("rooms", rooms);

        } catch (Exception e) {
            e.printStackTrace();
            setErrorTip("查询报修信息异常", "main.jsp", model);
            return "infoTip";
        }

        return "repairlistShow";
    }

    /**
     * @return String
     * @Title: addRepairlistShow
     * @Description: 显示添加报修页面
     */
    @RequestMapping(value = "admin/Admin_addRepairlistShow.action")
    public String addRepairlistShow(Repairlist paramsRepairlist, PaperUtil paperUtil,
                                    ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        //查询房屋字典
        User admin = (User) httpSession.getAttribute("admin");
        Room room = new Room();
        room.setStart(-1);
        room.setUser_id(admin.getUser_id());
        List<Room> rooms = adminManager.listRooms(room, null);
        if (rooms == null) {
            rooms = new ArrayList<Room>();
        }
        model.addAttribute("rooms", rooms);

        return "repairlistEdit";
    }

    /**
     * @return String
     * @Title: addRepairlist
     * @Description: 添加报修
     */
    @RequestMapping(value = "admin/Admin_addRepairlist.action")
    public String addRepairlist(Repairlist paramsRepairlist, PaperUtil paperUtil,
                                ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //添加报修
            adminManager.addRepairlist(paramsRepairlist);

            setSuccessTip("提交报修信息成功", "Admin_listRepairlists.action", model);
        } catch (Exception e) {
            setErrorTip("提交报修信息异常", "Admin_listRepairlists.action", model);
            e.printStackTrace();
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: editRepairlist
     * @Description: 编辑报修
     */
    @RequestMapping(value = "admin/Admin_editRepairlist.action")
    public String editRepairlist(Repairlist paramsRepairlist, PaperUtil paperUtil,
                                 ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //得到报修
            Repairlist repairlist = adminManager.queryRepairlist(paramsRepairlist);
            model.addAttribute("repairlist", repairlist);

        } catch (Exception e) {
            setErrorTip("查询报修信息异常", "Admin_listRepairlists.action", model);
            return "infoTip";
        }

        return "repairlistUpdate";
    }

    /**
     * @return String
     * @Title: saveRepairlist
     * @Description: 保存编辑报修
     */
    @RequestMapping(value = "admin/Admin_saveRepairlist.action")
    public String saveRepairlist(Repairlist paramsRepairlist, PaperUtil paperUtil,
                                 ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //保存编辑报修
            adminManager.updateRepairlist(paramsRepairlist);

            setSuccessTip("处理成功", "Admin_listRepairlists.action", model);
        } catch (Exception e) {
            e.printStackTrace();
            setErrorTip("处理失败", "Admin_listRepairlists.action", model);
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: delRepairlists
     * @Description: 删除报修
     */
    @RequestMapping(value = "admin/Admin_delRepairlists.action")
    public String delRepairlists(Repairlist paramsRepairlist, PaperUtil paperUtil,
                                 ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //删除报修
            adminManager.delRepairlists(paramsRepairlist);

            setSuccessTip("删除报修信息成功", "Admin_listRepairlists.action", model);
        } catch (Exception e) {
            setErrorTip("删除报修信息异常", "Admin_listRepairlists.action", model);
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: listSuggests
     * @Description: 查询意见建议
     */
    @RequestMapping(value = "admin/Admin_listSuggests.action")
    public String listSuggests(Suggest paramsSuggest, PaperUtil paperUtil,
                               ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            if (paramsSuggest == null) {
                paramsSuggest = new Suggest();
            }
            //设置分页信息
            paperUtil.setPagination(paramsSuggest);
            //总的条数
            int[] sum = {0};
            //用户身份限制
            User admin = (User) httpSession.getAttribute("admin");
            if (admin.getUser_type() == 1) {
                paramsSuggest.setUser_id(admin.getUser_id());
            }
            //查询意见建议列表
            List<Suggest> suggests = adminManager.listSuggests(paramsSuggest, sum);
            model.addAttribute("suggests", suggests);
            paperUtil.setTotalCount(sum[0]);
            model.addAttribute("paramsSuggest", paramsSuggest);

        } catch (Exception e) {
            e.printStackTrace();
            setErrorTip("查询意见建议异常", "main.jsp", model);
            return "infoTip";
        }

        return "suggestShow";
    }

    /**
     * @return String
     * @Title: addSuggestShow
     * @Description: 显示添加意见建议页面
     */
    @RequestMapping(value = "admin/Admin_addSuggestShow.action")
    public String addSuggestShow(Suggest paramsSuggest, PaperUtil paperUtil,
                                 ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        return "suggestEdit";
    }

    /**
     * @return String
     * @Title: addSuggest
     * @Description: 添加意见建议
     */
    @RequestMapping(value = "admin/Admin_addSuggest.action")
    public String addSuggest(Suggest paramsSuggest, PaperUtil paperUtil,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //添加报修
            adminManager.addSuggest(paramsSuggest);

            setSuccessTip("提交意见建议成功", "Admin_listSuggests.action", model);
        } catch (Exception e) {
            setErrorTip("提交意见建议异常", "Admin_listSuggests.action", model);
            e.printStackTrace();
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: editSuggest
     * @Description: 编辑意见建议
     */
    @RequestMapping(value = "admin/Admin_editSuggest.action")
    public String editSuggest(Suggest paramsSuggest, PaperUtil paperUtil,
                              ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //得到意见建议
            Suggest suggest = adminManager.querySuggest(paramsSuggest);
            model.addAttribute("suggest", suggest);

        } catch (Exception e) {
            setErrorTip("查询意见建议异常", "Admin_listSuggests.action", model);
            return "infoTip";
        }

        return "suggestUpdate";
    }

    /**
     * @return String
     * @Title: saveSuggest
     * @Description: 保存编辑意见建议
     */
    @RequestMapping(value = "admin/Admin_saveSuggest.action")
    public String saveSuggest(Suggest paramsSuggest, PaperUtil paperUtil,
                              ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //保存编辑意见建议
            adminManager.updateSuggest(paramsSuggest);

            setSuccessTip("回复成功", "Admin_listSuggests.action", model);
        } catch (Exception e) {
            e.printStackTrace();
            setErrorTip("回复失败", "Admin_listSuggests.action", model);
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: delSuggests
     * @Description: 删除报修
     */
    @RequestMapping(value = "admin/Admin_delSuggests.action")
    public String delSuggests(Suggest paramsSuggest, PaperUtil paperUtil,
                              ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //删除报修
            adminManager.delSuggests(paramsSuggest);

            setSuccessTip("删除意见建议成功", "Admin_listSuggests.action", model);
        } catch (Exception e) {
            setErrorTip("删除意见建议异常", "Admin_listSuggests.action", model);
        }

        return "infoTip";
    }

    /**
     * @return boolean
     * @Title: validateAdmin
     * @Description: admin登录验证
     */
    private boolean validateAdmin(HttpSession httpSession) {
        User admin = (User) httpSession.getAttribute("admin");
        if (admin != null) {
            return true;
        } else {
            return false;
        }
    }

    private void setErrorTip(String tip, String url, ModelMap model) {
        model.addAttribute("tipType", "error");
        model.addAttribute("tip", tip);
        model.addAttribute("url1", url);
        model.addAttribute("value1", "确 定");
    }

    private void setSuccessTip(String tip, String url, ModelMap model) {
        model.addAttribute("tipType", "success");
        model.addAttribute("tip", tip);
        model.addAttribute("url1", url);
        model.addAttribute("value1", "确 定");
    }


//=========================================人事管理================================
    /**
     * @return String
     * @Title: listWorkers
     * @Description: 查询员工
     */
    @RequestMapping(value = "admin/Admin_listPeoples.action")
    public String listPeoples(People paramsPeople, PaperUtil paperUtil,
                              ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            if (paramsPeople == null) {
                paramsPeople = new People();
            }
            //设置分页信息
            if (paperUtil == null) {
                paperUtil = new PaperUtil();
            }
            paperUtil.setPagination(paramsPeople);
            //总的条数
            int[] sum = {0};
            //查询员工列表
            List<People> peoples = adminManager.listPeoples(paramsPeople, sum);
            model.addAttribute("peoples", peoples);
            paperUtil.setTotalCount(sum[0]);
            model.addAttribute("paramsPeople", paramsPeople);

            //员工类型
//            model.addAttribute("worker_type", paramsPeople.getWorker_type());
//            model.addAttribute("worker_typeDesc", paramsPeople.getWorker_typeDesc());

        } catch (Exception e) {
//            setErrorTip("查询" + paramsPeople.getWorker_typeDesc() + "异常", "Admin_listWorkers.action?worker_type=" + paramsWorker.getWorker_type(), model);
            setErrorTip("查询人事信息异常", "main.jsp", model);
            return "infoTip";
        }
        return "peopleShow";
    }

    /**
     * @return String
     * @Title: queryWorker
     * @Description: 查询员工
     */
    @RequestMapping(value = "admin/Admin_queryPeople.action", method = RequestMethod.GET)
    public String queryPeople(People paramsPeople, ModelMap model) {
        try {
            //得到员工
            People people = adminManager.queryPeople(paramsPeople);
            model.addAttribute("people", people);

            //员工类型
//            model.addAttribute("worker_type", paramsWorker.getWorker_type());
//            model.addAttribute("worker_typeDesc", paramsWorker.getWorker_typeDesc());

        } catch (Exception e) {
//            setErrorTip("查询" + paramsWorker.getWorker_typeDesc() + "异常", "Admin_listWorkers.action?worker_type=" + paramsWorker.getWorker_type(), model);
            setErrorTip("查询人事信息异常", "main.jsp", model);
            return "infoTip";
        }

        return "peopleDetail";
    }

    /**
     * @return String
     * @Title: addWorkerShow
     * @Description: 显示添加员工页面
     */
    @RequestMapping(value = "admin/Admin_addPeopleShow.action")
    public String addPeopleShow() {
        return "peopleEdit";
    }

    /**
     * @return String
     * @Title: addWorker
     * @Description: 添加员工
     */
    @RequestMapping(value = "admin/Admin_addPeople.action", method = RequestMethod.POST)
    public String addPeople(People paramsPeople,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //添加员工
            adminManager.addPeople(paramsPeople);

            setSuccessTip("添加成功", "Admin_listPeoples.action", model);
        } catch (Exception e) {
            setErrorTip("添加异常", "Admin_listPeoples.action", model);
            e.printStackTrace();
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: editWorker
     * @Description: 编辑员工
     */
    @RequestMapping(value = "admin/Admin_editPeople.action")
    public String editPeople(People paramsPeople,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //得到员工
            People people = adminManager.queryPeople(paramsPeople);
            model.addAttribute("people", people);

        } catch (Exception e) {
            setErrorTip("查询人事信息异常", "Admin_listPeoples.action", model);
            return "infoTip";
        }

        return "peopleEdit";
    }

    /**
     * @return String
     * @Title: saveWorker
     * @Description: 保存编辑员工
     */
    @RequestMapping(value = "admin/Admin_savePeople.action")
    public String savePeople(People paramsPeople,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //保存编辑员工
            adminManager.updatePeople(paramsPeople);

            setSuccessTip("编辑成功", "Admin_listPeoples.action", model);
        } catch (Exception e) {
            tip = "编辑失败";
            model.addAttribute("people", paramsPeople);

            //员工类型
//            model.addAttribute("worker_type", paramsWorker.getWorker_type());
//            model.addAttribute("worker_typeDesc", paramsWorker.getWorker_typeDesc());

            return "peopleEdit";
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: delWorkers
     * @Description: 删除员工
     */
    @RequestMapping(value = "admin/Admin_delPeoples.action")
    public String delPeoples(String ids,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //删除员工
            adminManager.delPeoples(ids);

            setSuccessTip("删除成功","Admin_listPeoples.action", model);
        } catch (Exception e) {
            setErrorTip("删除异常", "Admin_listPeoples.action", model);
        }

        return "infoTip";
    }
    //========================================租赁房屋管理================================
    /**
     * @return String
     * @Title: listRents
     * @Description: 查询房屋缴费信息
     */
    @RequestMapping(value = "admin/Admin_listRents.action")
    public String listRents(Rent paramsRent, PaperUtil paperUtil,
                             ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            if (paramsRent == null) {
                paramsRent = new Rent();
            }
            //设置分页信息
            paperUtil.setPagination(paramsRent);
            //总的条数
            int[] sum = {0};
            //用户身份限制
            User admin = (User) httpSession.getAttribute("admin");
            if (admin.getUser_type() == 1) {
                paramsRent.setUser_id(admin.getUser_id());
            }
            //查询房屋缴费信息列表
            List<Rent> rents = adminManager.listRents(paramsRent, sum);
            model.addAttribute("rents", rents);
            paperUtil.setTotalCount(sum[0]);
            model.addAttribute("paramsRent", paramsRent);

            //查询房屋字典
            Room room = new Room();
            room.setStart(-1);
            List<Room> rooms = adminManager.listRooms(room, null);
            if (rooms == null) {
                rooms = new ArrayList<Room>();
            }
            model.addAttribute("rooms", rooms);

        } catch (Exception e) {
            e.printStackTrace();
            setErrorTip("查询房屋缴费信息异常", "main.jsp", model);
            return "infoTip";
        }

        return "rentShow";
    }

    /**
     * @return String
     * @Title: addRentShow
     * @Description: 显示添加房屋缴费信息页面
     */
    @RequestMapping(value = "admin/Admin_addRentShow.action")
    public String addRentShow(ModelMap model) {
        //查询房屋字典
        Room room = new Room();
        room.setStart(-1);
        List<Room> rooms = adminManager.listRooms(room, null);
        if (rooms == null) {
            rooms = new ArrayList<Room>();
        }
        model.addAttribute("rooms", rooms);

        return "rentEdit";
    }

    /**
     * @return String
     * @Title: addRent
     * @Description: 添加房屋缴费信息
     */
    @RequestMapping(value = "admin/Admin_addRent.action")
    public String addRent(Rent paramsRent,
                           ModelMap model) {
        try {
            //添加房屋缴费信息
            adminManager.addRent(paramsRent);

            setSuccessTip("提交成功", "Admin_listRents.action", model);
        } catch (Exception e) {
            e.printStackTrace();
            setErrorTip("提交房屋缴费信息异常", "Admin_listRents.action", model);
        }

        return "infoTip";
    }


    /**
     * @return String
     * @Title: editRent
     * @Description: 编辑房屋缴费信息
     */
    @RequestMapping(value = "admin/Admin_editRent.action")
    public String editRent(Rent paramsRent,
                            ModelMap model) {
        try {
            //得到房屋缴费信息
            Rent rent = adminManager.queryRent(paramsRent);
            model.addAttribute("rent", rent);

            //查询房屋字典
            Room room = new Room();
            room.setStart(-1);
            List<Room> rooms = adminManager.listRooms(room, null);
            if (rooms == null) {
                rooms = new ArrayList<Room>();
            }
            model.addAttribute("rooms", rooms);

        } catch (Exception e) {
            setErrorTip("查询房屋缴费信息异常", "Admin_listRents.action", model);
            return "infoTip";
        }

        return "rentEdit";
    }

    /**
     * @return String
     * @Title: saveRent
     * @Description: 保存编辑房屋缴费信息
     */
    @RequestMapping(value = "admin/Admin_saveRent.action")
    public String saveRent(Rent paramsRent,
                            ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        try {
            //保存编辑房屋缴费信息
            adminManager.updateRent(paramsRent);

            setSuccessTip("编辑成功", "Admin_listRents.action", model);
        } catch (Exception e) {
            setErrorTip("编辑失败", "Admin_listRents.action", model);
        }

        return "infoTip";
    }

    /**
     * @return String
     * @Title: delRents
     * @Description: 删除房屋缴费信息
     */
    @RequestMapping(value = "admin/Admin_delRents.action")
    public String delRents(String ids,
                            ModelMap model) {
        try {
            //删除房屋缴费信息
            adminManager.delRents(ids);

            setSuccessTip("删除房屋缴费信息成功", "Admin_listRents.action", model);
        } catch (Exception e) {
            setErrorTip("删除房屋缴费信息异常", "Admin_listRents.action", model);
        }

        return "infoTip";
    }

}
