package kotlinbook.jpashop.controller

class BookForm(
    var id: Long? = null,
    var name: String? = null,
    var price: Int? = 0,
    var stockQuantity: Int? = 0,
    var author: String? = null,
    var isbn: String? = null,
)