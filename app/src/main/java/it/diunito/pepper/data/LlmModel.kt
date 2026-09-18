package it.diunito.pepper.data

import it.diunito.pepper.R

enum class LlmModel(
    val displayName: String,
    val modelId: String,
    val iconResLight: Int,
    val iconResDark: Int
) {
    GPT_OSS(
        displayName = "ChatGPT",
        modelId = "SLURM.utopia/gpt-oss:120b",
        iconResLight = R.drawable.ic_chatgpt_black,
        iconResDark = R.drawable.ic_chatgpt_white
    ),
    DEEPSEEK(
        displayName = "DeepSeek",
        modelId = "SLURM.deepseek-r1:70b-llama-distill-q8_0",
        iconResLight = R.drawable.ic_deepseek,
        iconResDark = R.drawable.ic_deepseek
    )
}
