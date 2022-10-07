package com.gmail.pentominto.us.supernotes

fun main() {

    val lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Dui nunc mattis enim ut tellus elementum sagittis. Eu turpis egestas pretium aenean pharetra magna ac. Eget felis eget nunc lobortis mattis aliquam. Donec et odio pellentesque diam volutpat commodo. Vel eros donec ac odio tempor orci. Euismod elementum nisi quis eleifend quam adipiscing vitae proin. Sem et tortor consequat id porta nibh venenatis cras. Enim tortor at auctor urna. Nunc aliquet bibendum enim facilisis gravida. Tempus urna et pharetra pharetra massa massa. Ut venenatis tellus in metus. Sit amet luctus venenatis lectus magna fringilla urna. Non arcu risus quis varius quam.\n" +
            "\n" +
            "Egestas tellus rutrum tellus pellentesque eu tincidunt. Iaculis urna id volutpat lacus. Rhoncus urna neque viverra justo nec ultrices dui sapien. Ac orci phasellus egestas tellus rutrum tellus pellentesque eu. Fermentum odio eu feugiat pretium nibh ipsum consequat. Posuere ac ut consequat semper viverra. Elit pellentesque habitant morbi tristique senectus et netus et. Netus et malesuada fames ac turpis egestas. Eget aliquet nibh praesent tristique magna sit amet purus gravida. Nisl nunc mi ipsum faucibus vitae aliquet nec. Cras semper auctor neque vitae tempus quam pellentesque nec. Ac tincidunt vitae semper quis. Quam nulla porttitor massa id neque aliquam. Blandit aliquam etiam erat velit scelerisque in. Feugiat sed lectus vestibulum mattis ullamcorper velit. Viverra vitae congue eu consequat ac felis donec et. Placerat vestibulum lectus mauris ultrices eros. In ornare quam viverra orci sagittis eu volutpat odio."


    val chunked = lorem.chunked(25)

    chunked.forEach {
        if (it.contains("lorem", true)) {

            println(it)
        }
    }
}