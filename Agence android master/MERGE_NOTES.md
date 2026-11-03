# دمج تطبيق العميل وتطبيق العضو

## البنية
- المشروع مبني على تطبيق العميل، واسم الحزمة `com.ksa.agence`.
- كود العضو كامل صار داخل `app/src/main/java/com/ksa/agence/member/` بنفس تقسيمه القديم (adapter, base, common, di, entity, network, repository, ui, viewModels).
- كلاس التطبيق الوحيد `com.ksa.agence.app.AgenceApp`، ويشغّل جهة العضو عن طريق `AgenceCompanyApp.init(this)` ويحمّل موديولات Koin حق الاثنين.

## الموارد
- أي ملف أو قيمة عند العضو بنفس اسم ملف عند العميل ومحتواها مختلف أخذت بادئة `member_`.
  مثال: `fragment_home.xml` حق العضو صار `member_fragment_home.xml` والـ binding صار `MemberFragmentHomeBinding`.
- نص `title` حق العضو صار `member_title_m` لأن عند العميل نص اسمه `member_title` أصلاً.
- قيم العضو المختلفة (النصوص واللونين) موجودة في `res/values/member_values.xml` و `res/values-ar/member_values.xml`.
- ملفات الـ navigation حق العضو: `member_nav_graph.xml` و `member_nav_graph_auth.xml`.

## ملفات العميل الي تعدلت
- `app/src/main/java/com/ksa/agence/app/AgenceApp.kt`
- `app/src/main/AndroidManifest.xml`
- `app/build.gradle.kts` و `gradle/libs.versions.toml` و `settings.gradle.kts`

## الشبكة
- موديول Koin حق العضو يستخدم أسماء خاصة (`member_retrofit` و `member_okhttp` و `member_base_url`) عشان ما يطغى على Retrofit حق العميل.
- التفضيلات منفصلة حالياً: العميل `Agence_App` والعضو `AgenceCompany_App`.

## المرحلة الجاية
- توحيد تسجيل الدخول وتوجيه المستخدم حسب نوع حسابه (عميل أو عضو) من `SplashActivity`.
