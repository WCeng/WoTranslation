package com.wceng.network

import com.wceng.network.api.TranslateApi
import com.wceng.network.di.NetworkModule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class WoNetworkDataSourceTest {

    private lateinit var translateApi: TranslateApi

    @Before
    fun setupRetrofit() {
        translateApi = NetworkModule.provideRetrofit().create(TranslateApi::class.java)
    }

    @Test
    fun translateText_resultIsExpected() = runTest {
        val loaded = translateApi.translateText("en", "zh", "Hello")
        assert(loaded.errorCode == null)
        assert(loaded.targetText.contains("你好"))
    }

    @Test
    fun translate_when_originalTextIsVeryMuchLong_then_isReturnSuccess() = runTest {
        val loaded = translateApi.translateText("en", "zh", longOriginalText)

        println(loaded)
        assert(loaded.errorCode != null)
        assert(loaded.targetText.isNotEmpty())
    }

    @Test
    fun translateText_translateParamError() = runTest {
        val loaded = translateApi.translateText("asf", "zh", "Hello")
        assert(loaded.errorCode != null)
    }

}


private const val longOriginalText =
    "New round of stimulus steps anticipated\n" +
            "Economists: Stronger pro-growth stance, increased focus on consumption likely\n" +
            "By OUYANG SHIJIA/ZHOU LANXU | China Daily | Updated: 2025-02-27 06:55\n" +
            "Chinese policymakers will likely adopt a more pro-growth stance and strengthen \"extraordinary countercyclical measures\" to spur the economy amid pressing challenges from sluggish domestic demand and external uncertainties, economists said.\n" +
            "\n" +
            "They anticipate a new round of stimulus packages focusing on driving domestic demand and boosting consumption to address domestic structural issues, offset the rising headwinds from a grimmer global environment and foster sustainable, high-quality growth in the long run.\n" +
            "\n" +
            "The economists also said they believe that measures in the pipeline may include more public borrowing and spending, with a growing shift of policy emphasis to consumption, enhanced financial support for low-income households, and increased spending on the property sector through the buying back of land or by helping developers finish presold homes.\n" +
            "\n" +
            "Their comments came as the market is closely watching how China will draw up growth targets and policies to revive the world's second-largest economy when China's top legislative and political advisory bodies meet for the annual two sessions in early March.\n" +
            "\n" +
            "Noting that the broader economy is still facing pressures from a harsher external environment and still-weak domestic demand, Sun Xuegong, director of the department of policy study and consultation at the Chinese Academy of Macroeconomic Research, said that policymakers will likely introduce a package of stimulus measures aimed at boosting market confidence and stabilizing expectations. The academy is part of the National Development and Reform Commission, the country's top economic regulator.\n" +
            "\n" +
            "As authorities pledged to implement extraordinary countercyclical policies this year, Sun highlighted the need to expand fiscal expenditure, with policy focus shifting to spurring consumption.\n" +
            "\n" +
            "\"We need a comprehensive policy mix to boost consumption,\" Sun said in an interview with China Daily. \"The government has already announced raising the pension level and providing subsidies to the low-income group. And this year, the country will extend the program of trade-in deals for consumer goods and expand the scope to more fields of consumption.\"\n" +
            "\n" +
            "With a series of existing policies taking effect gradually and more supportive measures in the pipeline, Sun said he expects to see a pickup in consumption.\n" +
            "\n" +
            "At a study session held last week by the State Council, China's Cabinet, Premier Li Qiang emphasized boosting consumption and improving people's livelihoods through stronger and more targeted measures, in a bid to strengthen the fundamental role of consumption in driving economic development.\n" +
            "\n" +
            "Wang Tao, chief China economist at UBS Investment Bank, said her team expects the government to ramp up fiscal spending to support consumption and the household sector, including more than doubling the size of the trade-in program to over 300 billion yuan (\$41.3 billion), creating a subsidy program for families with young children, and increasing the payout level of residents' basic pension and the government contribution to basic social insurance.\n" +
            "\n" +
            "\"These much-anticipated measures could gradually help underpin household confidence and unleash consumption potential in the long run,\" Wang said.\n" +
            "\n" +
            "On the monetary front, Wang said she anticipates that the two sessions will follow the \"moderately loose\" monetary policy tone set during the Central Economic Work Conference in December, with an explicit call for lowering the funding cost of corporate financing and household credit, cutting reserve requirement ratios and policy rates and enhancing counter cyclical adjustments.\n" +
            "\n" +
            "Tian Xuan, associate dean of Tsinghua University's PBC School of Finance, said this year's economic growth target will likely remain at \"around 5 percent\", the same as last year, reflecting the continuity and consistency of policies and aligning with the country's aspiration that its per capita GDP would reach the level of a moderately developed economy by 2035.\n" +
            "\n" +
            "The expectation of an unchanged growth target comes as China's major economic hubs, such as Beijing and Shanghai as well as Guangdong province, have announced GDP growth goals of around 5 percent for the year.\n" +
            "\n" +
            "\"It would be a goal that we need to strive for and reach with extra effort, which can effectively inspire all the people across the country to work hard together,\" said Tian, who expects this year's budget deficit-to-GDP ratio to increase to 4 percent or higher.\n" +
            "\n" +
            "Lu Ting, chief China economist at Nomura, said a higher fiscal deficit ratio would allow the central government to issue more bonds, ramp up transfers to local governments and alleviate fiscal pressures.\n" +
            "\n" +
            "Lu said the fiscal stimulus package will likely focus on areas including funding for a trade-in program for equipment upgrades and consumer goods, spending on the property sector through the buying back of land or by helping developers finish presold homes, and financial support for low-income households as well as funding to encourage childbirth.\n" +
            "\n" +
            "\"The Chinese economy may get off to a relatively good start. … We expect year-on-year real GDP growth to stay at 5 percent in the first quarter,\" he said.\n" +
            "\n" +
            "The breakthrough of China's homegrown artificial intelligence model DeepSeek has triggered a stock rally that may boost investment and consumption, while the expansion of the trade-in program is stimulating sales of digital goods, he added.\n" +
            "\n" +
            "Contact the writers at ouyangshijia@chinadaily.com.cn"