//
// Created by Administrator on 2019/12/28.
//

#ifndef LEARNING_IMAGE_PROCESS_IMAGECONSTANT_H
#define LEARNING_IMAGE_PROCESS_IMAGECONSTANT_H

#define RGB565_R(p) ((((p) & 0xF800) >> 11) << 3)
#define RGB565_G(p) ((((p) & 0x7E0 ) >> 5)  << 2)
#define RGB565_B(p) ( ((p) & 0x1F  )        << 3)
#define MAKE_RGB565(r, g, b) ((((r) >> 3) << 11) | (((g) >> 2) << 5) | ((b) >> 3))

#define RGBA_A(p) (((p) & 0xFF000000) >> 24)
#define RGBA_R(p) (((p) & 0x00FF0000) >> 16)
#define RGBA_G(p) (((p) & 0x0000FF00) >>  8)
#define RGBA_B(p)  ((p) & 0x000000FF)
#define MAKE_RGBA(r, g, b, a) (((a) << 24) | ((r) << 16) | ((g) << 8) | (b))

#endif //LEARNING_IMAGE_PROCESS_IMAGECONSTANT_H
