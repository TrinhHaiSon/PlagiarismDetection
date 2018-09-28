# -*- coding: utf-8 -*-
import re

MucLuc_text1 = u'(|  |\n *)(MỤC LỤC|CONTENTS|TABLE OF CONTENTS|MỤC LỤC) *\n'
MucLuc_text2 = u'(|  |\n *)(Mục Lục|Contents|Table Of Contents|Mục Lục) *\n'
MucLuc_text3 = u'(|  |\n *)(Mục lục|Contents|Table of contents|Mục lục) *\n'

TomTat_text1 = u'(|  |\n *)(TÓM TẮT NỘI DUNG ĐỒ ÁN TỐT NGHIỆP|TÓM TẮT ĐỒ ÁN|TÓM TẮT NỘI DUNG ĐỒ ÁN:?|TÓM TẮT|TÓM TẮT NỘI DUNG ĐỒ ÁN TỐT NGHIỆP) *\n'
TomTat_text2 = u'(|  |\n *)(Tóm Tắt Nội Dung Đồ Án Tốt Nghiệp|Tóm Tắt Đồ Án|Tóm Tắt Nội Dung Đồ Án:?|Tóm Tắt|Tóm Tắt Nội Dung Đồ Án Tốt Nghiệp) *\n'
TomTat_text3 = u'(|  |\n *)(Tóm tắt nội dung đồ án tốt nghiệp|Tóm tắt đồ án|Tóm tắt nội dung đồ án:?|Tóm tắt nội dung đồ án tốt nghiệp) *\n'

MoDau_text1 = u'(|  |\n *)((LỜI|PHẦN) (MỞ|NÓI) ĐẦU *\n|MỞ ĐẦU *\n)'
MoDau_text2 = u'(|  |\n *)((Lời|Phần) (Mở|Nói) Đầu *\n|Mở Đầu *\n)'
MoDau_text3 = u'(|  |\n *)((Lời|Phần) (mở|nói) đầu *\n|Mở đầu *\n)'

KetThuc_text1 = u'(|  |\n *)(MỤC LỤC|CONTENTS|TABLE OF CONTENTS|MỤC LỤC|LỜI CẢM ƠN|(LỜI|PHẦN) (MỞ|NÓI) ĐẦU|MỞ ĐẦU|BẢNG PHÂN CÔNG CÔNG VIỆC|ABSTRACT OF THESIS|GIỚI THIỆU CHUNG|DANH MỤC.*|PHIẾU GIAO.*|ABSTRACT|LỜI C[ẢÁ]M ƠN|LỜI CÁM ƠN|(CHƯƠNG|PHẦN) (1|MỘT|I)) *\n|(|  |\n *)(CHƯƠNG|PHẦN) (1[^0-9]|MỘT|I[^I]).*\n'
KetThuc_text2 = u'(|  |\n *)(Mục Lục|Contents|Table Of Contents|Mục Lục|Lời Cảm Ơn|(Lời|Phần) (Mở|Nói) Đầu|Mở Đầu|Bảng Phân Công Công Việc|Abstract Of Thesis|Giới Thiệu Chung|Danh Mục.*|Phiếu Giao.*|Abstract|Lời C[ảá]m Ơn|Lời Cám Ơn|(Chương|Phần) (1|Một|i)) *\n|(|  |\n *)(Chương|Phần) (1[^0-9]|Một|I[^I]).*\n'
KetThuc_text3 = u'(|  |\n *)(Mục lục|Contents|Table of contents|Mục lục|Lời cảm ơn|(Lời|Phần) (mở|nói) đầu|Mở đầu|Bảng phân công công việc|Abstract of thesis|Giới thiệu chung|Danh mục.*|phiếu giao.*|Abstract|Lời c[ảá]m ơn|Lời cám ơn|(Chương|Phần) (1|một|i)) *\n|(|  |\n *)(Chương|Phần) (1[^0-9]|một|i[^i]).*\n'

Chuong1_text1 = u'(|  |\n *)(CHƯƠNG|PHẦN) (1|MỘT|I) *\n|(|  |\n *)(CHƯƠNG|PHẦN) (1[^0-9]|MỘT|I[^I]).*\n'
Chuong1_text2 = u'(|  |\n *)(Chương|Phần) (1|Một|I) *\n'
Chuong1_text3 = u'(|  |\n *)(Chương|Phần) (1[^0-9]|một|I[^I]).*\n'
Chuong1_text4 = u'(|  |\n *)(1[^0-9]|I[^I]).*\n'

Chuong2_text1 = u'(|  |\n *)(CHƯƠNG|PHẦN) (2|HAI|II) *\n|(|  |\n *)(CHƯƠNG|PHẦN) (2[^0-9]|HAI|II[^I]).*\n'
Chuong2_text2 = u'(|  |\n *)(Chương|Phần) (2|Hai|II) *\n'
Chuong2_text3 = u'(|  |\n *)(Chương|Phần) (2[^0-9]|hai|II[^I]).*\n'
Chuong2_text4 = u'(|  |\n *)(2[^0-9]|II[^I]).*\n'

TomTat_text = [TomTat_text1, TomTat_text2, TomTat_text3]
MoDau_text = [MoDau_text1, MoDau_text2, MoDau_text3]
Chuong1_text = [Chuong1_text1, Chuong1_text2, Chuong1_text3, Chuong1_text4]
Chuong2_text = [Chuong2_text1, Chuong2_text2, Chuong2_text3, Chuong2_text4]
KetThuc_text = [KetThuc_text1, KetThuc_text2, KetThuc_text3, Chuong1_text4]
MucLuc_text = [MucLuc_text1, MucLuc_text2, MucLuc_text3]

def nomalize(text):
    text = re.sub(u'[sS]inh viên thực hiện.*\n',' ',text)
    t = re.sub(u'\n+', u'\n', text)
    return t

def split(text):
    text = re.sub(u'\n+| +', u' ', text)
    l = re.split(u'[\.!?]( |\n)',text)
    s = u''
    for sentence in l:
        if re.match(u'[^A-Za-zăâáắấảẳẩàằầạặậãẵẫđéèẹẻẽíìịỉĩóòọỏõớờơợởỡôồốổỗộụủũùúưứừữửựêếềệểễýĂÂÁẮẤẢẲẨÀẰẦẠẶẬÃẴẪĐÉÈẸẺẼÍÌỊỈĨÓÒỌỎÕỚỜƠỢỞỠÔỒỐỔỖỘỤỦŨÙÚƯỨỪỮỬỰÊẾỀỆỂỄÝ]*$',sentence):
            continue
        sentence = re.sub(u'[\[\]\'\"\(\)\-\\/,“”–•:\+|]|-|\.|||;|✓|←|❖|➢',u' ',sentence)
        sentence = re.sub(u'^ +', u'', sentence)
        sentence = re.sub(u' +', u' ', sentence)
        s = s + sentence + u'\n'
    return s

def search(list, text):
    for i in list:
        m = re.search(i, text)
        if m != None:
            return m
    return m

def getAbstract(text):
    MucLuc = search(MucLuc_text, text)
    if MucLuc != None:
        TruocMucLuc = text[:MucLuc.end()]
        SauMucLuc = text[MucLuc.end():]
        TomTat = search(TomTat_text, TruocMucLuc)
        MoDau = search(MoDau_text, TruocMucLuc)
        if TomTat != None:
            KetThucTomTat = search(KetThuc_text, TruocMucLuc[TomTat.end():])
            return TruocMucLuc[TomTat.end():TomTat.end() + KetThucTomTat.start()]
        elif MoDau != None:
            KetThucMoDau = search(KetThuc_text, TruocMucLuc[MoDau.end():])
            return TruocMucLuc[MoDau.end():MoDau.end() + KetThucMoDau.start()]
        else:
            TomTat_ = search(TomTat_text, SauMucLuc)
            MoDau_ = search(MoDau_text, SauMucLuc)
            Chuong1_ = search(Chuong1_text, SauMucLuc)
            if TomTat_ != None:
                try:
                    TomTat__ = search(TomTat_text, SauMucLuc[TomTat_.end():])
                    KetThucTomTat = search(KetThuc_text, SauMucLuc[TomTat_.end() + TomTat__.end():])
                    return SauMucLuc[
                           TomTat_.end() + TomTat__.end():TomTat_.end() + TomTat__.end() + KetThucTomTat.start()]
                except Exception:
                    KetThucTomTat = search(KetThuc_text, SauMucLuc[TomTat_.end():])
                    return SauMucLuc[TomTat_.end():TomTat_.end() + KetThucTomTat.start()]

            elif MoDau_ != None:
                try:
                    MoDau__ = search(MoDau_text, SauMucLuc[MoDau_.end():])
                    KetThucMoDau = search(KetThuc_text, SauMucLuc[MoDau_.end() + MoDau__.end():])
                    return SauMucLuc[MoDau_.end() + MoDau__.end():MoDau_.end() + MoDau__.end() + KetThucMoDau.start()]
                except Exception:
                    KetThucMoDau = search(KetThuc_text, SauMucLuc[MoDau_.end():])
                    return SauMucLuc[MoDau_.end():MoDau_.end() + KetThucMoDau.start()]

            elif Chuong1_ != None:
                d = search(Chuong2_text, SauMucLuc)
                try:
                    m = search(Chuong1_text, SauMucLuc[d.end():])
                    n = search(Chuong2_text, SauMucLuc[d.end() + m.end():])
                    return SauMucLuc[d.end() + m.end():d.end() + m.end() + n.start()]
                except:
                    return SauMucLuc[Chuong1_.end():d.start()]
            else:
                return None
    else:
        TomTat_ = search(TomTat_text, text)
        MoDau_ = search(MoDau_text, text)
        Chuong1_ = search(Chuong1_text, text)
        if TomTat_ != None:
            try:
                TomTat__ = search(TomTat_text, text[TomTat_.end():])
                KetThucTomTat = search(KetThuc_text, text[TomTat_.end() + TomTat__.end():])
                return text[TomTat_.end() + TomTat__.end():TomTat_.end() + TomTat__.end() + KetThucTomTat.start()]
            except Exception:
                KetThucTomTat = search(KetThuc_text, text[TomTat_.end():])
                return text[TomTat_.end():TomTat_.end() + KetThucTomTat.start()]

        elif MoDau_ != None:
            try:
                MoDau__ = search(MoDau_text, text[MoDau_.end():])
                KetThucMoDau = search(KetThuc_text, text[MoDau_.end() + MoDau__.end():])
                return text[MoDau_.end() + MoDau__.end():MoDau_.end() + MoDau__.end() + KetThucMoDau.start()]
            except Exception:
                KetThucMoDau = search(KetThuc_text, text[MoDau_.end():])
                return text[MoDau_.end():MoDau_.end() + KetThucMoDau.start()]

        elif Chuong1_ != None:
            d = search(Chuong2_text, text)
            try:
                m = search(Chuong1_text, text[d.end():])
                n = search(Chuong2_text, text[d.end() + m.end():])
                return text[d.end() + m.end():d.end() + m.end() + n.start()]
            except:
                return text[Chuong1_.end():d.start()]
        else:
            return None


def getContent(text):
    content = u''
    for m in re.finditer(u'[0-9]\..*\n',text):
        line = m.group(0)
        if re.search(u'[A-Za-zăâáắấảẳẩàằầạặậãẵẫđéèẹẻẽíìịỉĩóòọỏõớờơợởỡôồốổỗộụủũùúưứừữửựêếềệểễýĂÂÁẮẤẢẲẨÀẰẦẠẶẬÃẴẪĐÉÈẸẺẼÍÌỊỈĨÓÒỌỎÕỚỜƠỢỞỠÔỒỐỔỖỘỤỦŨÙÚƯỨỪỮỬỰÊẾỀỆỂỄÝ]',line):
            line = re.sub(u'^[^A-Za-zăâáắấảẳẩàằầạặậãẵẫđéèẹẻẽíìịỉĩóòọỏõớờơợởỡôồốổỗộụủũùúưứừữửựêếềệểễýĂÂÁẮẤẢẲẨÀẰẦẠẶẬÃẴẪĐÉÈẸẺẼÍÌỊỈĨÓÒỌỎÕỚỜƠỢỞỠÔỒỐỔỖỘỤỦŨÙÚƯỨỪỮỬỰÊẾỀỆỂỄÝ]*', '', line)
            line = re.sub(u'[^A-Za-zăâáắấảẳẩàằầạặậãẵẫđéèẹẻẽíìịỉĩóòọỏõớờơợởỡôồốổỗộụủũùúưứừữửựêếềệểễýĂÂÁẮẤẢẲẨÀẰẦẠẶẬÃẴẪĐÉÈẸẺẼÍÌỊỈĨÓÒỌỎÕỚỜƠỢỞỠÔỒỐỔỖỘỤỦŨÙÚƯỨỪỮỬỰÊẾỀỆỂỄÝ]*$', '', line)
            if re.match(u'bảng|hình|danh sách|biểu đồ|use case|danh mục',line.lower()):
                continue
            if re.search(u'[^A-Za-zăâáắấảẳẩàằầạặậãẵẫđéèẹẻẽíìịỉĩóòọỏõớờơợởỡôồốổỗộụủũùúưứừữửựêếềệểễýĂÂÁẮẤẢẲẨÀẰẦẠẶẬÃẴẪĐÉÈẸẺẼÍÌỊỈĨÓÒỌỎÕỚỜƠỢỞỠÔỒỐỔỖỘỤỦŨÙÚƯỨỪỮỬỰÊẾỀỆỂỄÝ0-9 \-\.\?\!]',line):
                continue
            if len(line) < 10:
                continue
            content = re.sub(u'[\[\]\'\"\(\)\-\\/,“”–•:\+|]|-|\.||▪||;|✓|←', u' ', content)
            content = re.sub(u'^ +', u'', content)
            content = re.sub(u' +', u' ', content)
            content = content + line + '\n'
    return content

