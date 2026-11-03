package com.ksa.agence.common

import com.ksa.agence.app.AgenceApp

/**
 * English versions of the sample (demo) data shown while the server has no real data.
 * Each sample text is written in Arabic in the code and wrapped with sampleText(...),
 * so it follows the app language. Delete this file together with the demo data
 * once the screens are connected to the server.
 */
object SampleText {

    private val EN = mapOf(
        // people and companies
        "فهد العتيبي" to "Fahad Al-Otaibi",
        "نجم للتسويق" to "Najm Marketing",
        "خالد المنصوري" to "Khalid Al-Mansouri",
        "أثير سعودي" to "Atheer Saudi",
        "أثر ستوديو" to "Athar Studio",
        "استديو أثر" to "Athar Studio",
        "وكالة نجم" to "Najm Agency",
        "وكالة الرواد للتسويق" to "Al-Ruwwad Marketing Agency",
        "صيدلية المدينة" to "Al-Madina Pharmacy",
        "الفارس للتجزئة" to "Al-Faris Retail",
        "تِك فيجن" to "Tech Vision",
        "نون ستور" to "Noon Store",
        "مؤسسة الإبداع" to "Al-Ibdaa Est.",
        "شركة رياض التقنية" to "Riyadh Tech",
        "شركة الرياض التقنية" to "Riyadh Tech",
        "الرياض، المملكة العربية السعودية" to "Riyadh, Saudi Arabia",

        // order preview
        "5,750 ر.س" to "SAR 5,750",
        "أسبوعان" to "2 weeks",
        "14 يوليو 2026" to "14 July 2026",
        "جلسة تعارف واستكشاف احتياجاتك" to "Discovery call and briefing session",
        "بحث وتحليل المنافسين في السوق" to "Market research and competitor analysis",
        "عرض التصورات الأولية (اتجاهان)" to "Initial concepts presented (2 directions)",
        "جولات المراجعة (حتى جولتين)" to "Revision rounds (up to 2 rounds)",
        "التسليم النهائي ودليل الهوية PDF" to "Final delivery and brand guidelines PDF",

        // activity log
        "أكمل مهمة" to "completed the task",
        "عرض المفاهيم الأولية" to "Present initial concepts",
        "قبل ساعتين" to "2 hours ago",
        "اليوم" to "Today",
        "أضاف تعليقاً على مهمة" to "commented on the task",
        "تحليل المنافسين" to "Competitor analysis",
        "قبل 4 ساعات" to "4 hours ago",
        "عدّل توزيع المدفوعات" to "updated the payment distribution",
        "قبل 6 ساعات" to "6 hours ago",
        "أنشأ مهمة جديدة" to "created a new task",
        "جلسة المراجعة النهائية" to "Final review session",
        "أمس 3:50 م" to "Yesterday 3:50 PM",
        "أمس" to "Yesterday",
        "رفع ملفاً في" to "uploaded a file to",
        "تصميم الهوية" to "Identity design",
        "أمس 11:00 ص" to "Yesterday 11:00 AM",
        "دعا" to "invited",
        "أثير سعودي للفريق" to "Atheer Saudi to the team",
        "قبل 3 أيام" to "3 days ago",
        "هذا الأسبوع" to "This week",

        // team leader panel
        "تحليل الهوية الحالية والمنافسين" to "Analyze the current identity and competitors",
        "غداً · ٣٠ يوليو" to "Tomorrow · 30 July",
        "٢٥ يوليو" to "25 July",
        "تصميم الهوية البصرية النهائية" to "Final visual identity design",
        "متأخرة يومان" to "2 days late",
        "جلسة المراجعة النهائية مع العميل" to "Final review session with the client",
        "٣ أغسطس" to "3 August",

        // payment distribution and permissions
        "استراتيجية العلامة - قائد" to "Brand strategy · Leader",
        "الحملات المدفوعة - عضو" to "Paid campaigns · Member",
        "الهوية البصرية - عضو" to "Visual identity · Member",
        "استراتيجية العلامة التجارية" to "Brand strategy",
        "الحملات الممولة" to "Sponsored campaigns",
        "الهوية البصرية" to "Visual identity",

        // opportunities
        "إعداد حملة إعلانات جوجل" to "Set up a Google Ads campaign",
        "ريال 150/hr" to "SAR 150/hr",
        "150 ريال" to "SAR 150",
        "الموعد النهائي: 15 أغسطس 2026" to "Deadline: 15 August 2026",
        "تصميم هوية العلامة التجارية" to "Brand identity design",
        "8,500 ريال" to "SAR 8,500",
        "الموعد النهائي: 20 أغسطس 2026" to "Deadline: 20 August 2026",
        "استراتيجية سوشيال الربع الثالث" to "Q3 social media strategy",
        "6,000 ريال" to "SAR 6,000",
        "الموعد النهائي: 30 أغسطس 2026" to "Deadline: 30 August 2026",
        "إدارة متجر إلكتروني" to "Online store management",
        "4,500 ريال" to "SAR 4,500",
        "الموعد النهائي: 5 سبتمبر 2026" to "Deadline: 5 September 2026",
        "كتابة محتوى تسويقي" to "Marketing copywriting",
        "ريال 90/hr" to "SAR 90/hr",
        "الموعد النهائي: 12 سبتمبر 2026" to "Deadline: 12 September 2026",

        // member home
        "هوية العلامة التجارية والتموضع" to "Brand identity and positioning",
        "5 أعضاء" to "5 members",
        "حملة سوشيال ميديا الربع الثالث" to "Q3 social media campaign",
        "3 أعضاء" to "3 members",
        "الهوية البصرية وتحديد الموقع" to "Visual identity and positioning",
        "12,000 ر.س" to "SAR 12,000",
        "نشط" to "Active",
        "8,500 ر.س" to "SAR 8,500",
        "قيد المراجعة" to "Under review",
        "إعداد وإدارة إعلانات جوجل" to "Google Ads setup and management",
        "4,200 ر.س" to "SAR 4,200",
        "24,500 ريال" to "24,500 SAR",

        // member orders
        "تدقيق العلامة التجارية والتموضع" to "Brand audit and positioning",
        "2 يوليو 2026" to "2 July 2026",
        "5,000 ريال" to "SAR 5,000",
        "تدقيق إعلانات ميتا" to "Meta ads audit",
        "28 يونيو 2026" to "28 June 2026",
        "150 ريال/hr" to "SAR 150/hr",
        "الشعار ونظام الهوية" to "Logo and identity system",
        "10 مايو 2026" to "10 May 2026",
        "4,100 ريال" to "SAR 4,100",
    )

    fun isEnglish(): Boolean {
        val context = AgenceApp.context ?: return false
        return AppLanguage.current(context) == "en"
    }

    fun get(arabic: String): String = if (isEnglish()) EN[arabic] ?: arabic else arabic
}

/** Short form used inside the demo data lists. */
fun sampleText(arabic: String): String = SampleText.get(arabic)
