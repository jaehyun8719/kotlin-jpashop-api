package kotlinbook.jpashop.controller

import kotlinbook.jpashop.domain.Member
import kotlinbook.jpashop.domain.Order
import kotlinbook.jpashop.domain.item.Item
import kotlinbook.jpashop.repository.OrderSearch
import kotlinbook.jpashop.service.ItemService
import kotlinbook.jpashop.service.MemberService
import kotlinbook.jpashop.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class OrderController(
    @Autowired private val orderService: OrderService,
    @Autowired private val memberService: MemberService,
    @Autowired private val itemService: ItemService,
) {
    @GetMapping("/order")
    fun createForm(model: Model): String {
        val members: List<Member> = memberService.findMembers()
        val items: List<Item> = itemService.findItems()
        model.addAttribute("members", members)
        model.addAttribute("items", items)
        return "order/orderForm"
    }

    @PostMapping("/order")
    fun order(
        @RequestParam("memberId") memberId: Long,
        @RequestParam("itemId") itemId: Long,
        @RequestParam("count") count: Int
    ): String {
        orderService.order(memberId, itemId, count)
        return "redirect:/orders"
    }

    @GetMapping("/orders")
    fun orderList(@ModelAttribute("orderSearch") orderSearch: OrderSearch, model: Model): String {
        val orders: List<Order> = orderService.findOrders(orderSearch)
        model.addAttribute("orders", orders)
        return "order/orderList"
    }

    @PostMapping("/orders/{orderId}/cancel")
    fun cancelOrder(@PathVariable("orderId") orderId: Long): String {
        orderService.cancelOrder(orderId)
        return "redirect:/orders"
    }
}