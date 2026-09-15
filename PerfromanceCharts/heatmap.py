import os
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import seaborn as sns

# ---------------------------------------------------------------------------
# Load data files (V1 = Array-backed, V2 = Linked-list)
# ---------------------------------------------------------------------------
array_csv = "CustomQueue_wide_matrix.csv"
linked_csv = "CustomQueueV2_wide_matrix.csv"

array_df = pd.read_csv(array_csv, sep=";", engine="python")
linked_df = pd.read_csv(linked_csv, sep=";", engine="python")

# Clean headers
array_df.columns = [c.replace('"', "").strip() for c in array_df.columns]
linked_df.columns = [c.replace('"', "").strip() for c in linked_df.columns]

sizes = array_df["Size"].tolist()
methods = [c for c in array_df.columns if c != "Size"]

heatmap_data = np.zeros((len(methods), len(sizes)))
text_labels = []

for i, m in enumerate(methods):
    row_labels = []
    for j, size in enumerate(sizes):
        v1_val = array_df.loc[array_df["Size"] == size, m].values[0]
        v2_val = linked_df.loc[linked_df["Size"] == size, m].values[0]

        if v1_val == 0:
            v1_val = 1
        if v2_val == 0:
            v2_val = 1

        # log2 ratio: positive means V2 is faster (V1 took more time)
        # negative means V1 is faster (V2 took more time)
        ratio = np.log2(v1_val / v2_val)
        heatmap_data[i, j] = ratio

        if v1_val >= v2_val:
            factor = v1_val / v2_val
            row_labels.append(f"+{factor:.1f}x" if factor < 100 else f"+{factor:.0f}x")
        else:
            factor = v2_val / v1_val
            row_labels.append(f"-{factor:.1f}x" if factor < 100 else f"-{factor:.0f}x")
    text_labels.append(row_labels)

text_labels = np.array(text_labels)

# Sort methods by average performance ratio
avg_ratios = np.mean(heatmap_data, axis=1)
sorted_idx = np.argsort(avg_ratios)

heatmap_data = heatmap_data[sorted_idx]
text_labels = text_labels[sorted_idx]
sorted_methods = [methods[idx] for idx in sorted_idx]

# Plotting the heatmap - dynamically sized based on method count
fig, ax = plt.subplots(
    figsize=(14, max(8, len(methods) * 0.5)), facecolor="none"
)
ax.set_facecolor("none")

clipped_data = np.clip(heatmap_data, -4.0, 4.0)
cmap = sns.diverging_palette(15, 240, as_cmap=True)

sns.heatmap(
    clipped_data,
    annot=text_labels,
    fmt="",
    cmap=cmap,
    center=0,
    xticklabels=sizes,
    yticklabels=sorted_methods,
    ax=ax,
    cbar_kws={
        "label": (
            "← V1 (Array) Faster  |  Relative Speedup Scale (Clipped at"
            " 16x)  |  V2 (Linked) Faster →"
        )
    },
    linewidths=0.5,
    linecolor="#444444",
    annot_kws={"size": 9, "weight": "bold"},
)

ax.set_title(
    "V1 vs V2 Performance Comparison Matrix Heatmap\n(Positive/Blue = V2"
    " Faster, Negative/Red = V1 Faster)",
    color="#ffffff",
    fontsize=16,
    fontweight="bold",
    pad=25,
)
ax.set_ylabel(
    "Queue / Collection Interface Methods",
    color="#aaaaaa",
    fontsize=13,
    labelpad=12,
)
ax.set_xlabel(
    "Collection Size (Elements)", color="#aaaaaa", fontsize=13, labelpad=12
)

ax.tick_params(colors="#ffffff", labelsize=11)
plt.xticks(rotation=45)
plt.yticks(rotation=0)

cbar = ax.collections[0].colorbar
cbar.ax.tick_params(colors="#ffffff", labelsize=10)
cbar.ax.yaxis.label.set_color("#ffffff")
cbar.ax.yaxis.label.set_fontsize(12)

plt.tight_layout()
output_filename = "heatmap.png"
plt.savefig(output_filename, dpi=300, transparent=True)
plt.close()

print(f"Heatmap saved successfully as {output_filename}")
print("Top 5 worst performing methods for Custom V2 on average:")
print(sorted_methods[:5])
print("Top 5 best performing methods for Custom V2 on average:")
print(sorted_methods[-5:])