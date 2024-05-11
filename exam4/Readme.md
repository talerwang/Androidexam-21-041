```python
%matplotlib inline
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

```


```python
df = pd.read_csv('/kaggle/input/fortune500/fortune500.csv')

```


```python
df.head()
```


![image](https://github.com/talerwang/Androidexam-21-041/assets/155062674/ec3382c3-14c5-4696-9948-1a573f334712)





```python
df.tail()
```


![image](https://github.com/talerwang/Androidexam-21-041/assets/155062674/1e0fdf0d-9547-49d4-83f9-af0cb2aae6f0)






```python
df.columns = ['year', 'rank', 'company', 'revenue', 'profit']
```


```python
len(df)
```




    25500




```python
df.dtypes
```




    year         int64
    rank         int64
    company     object
    revenue    float64
    profit      object
    dtype: object




```python
non_numberic_profits = df.profit.str.contains('[^0-9.-]')
df.loc[non_numberic_profits].head()
```




![image](https://github.com/talerwang/Androidexam-21-041/assets/155062674/4ccbeb10-8ff3-4b60-a7e3-3b18a44e1e08)



```python
len(df.profit[non_numberic_profits])
```




    369




```python
bin_sizes, _, _ = plt.hist(df.year[non_numberic_profits], bins=range(1955, 2006))
```


    
![png](Readme_files/Readme_9_0.png)
    



```python
df = df.loc[~non_numberic_profits]
df.profit = df.profit.apply(pd.to_numeric)
```


```python
len(df)
```




    25131




```python
df.dtypes
```




    year         int64
    rank         int64
    company     object
    revenue    float64
    profit     float64
    dtype: object




```python
group_by_year = df.loc[:, ['year', 'revenue', 'profit']].groupby('year')
avgs = group_by_year.mean()
x = avgs.index
y1 = avgs.profit
def plot(x, y, ax, title, y_label):
    ax.set_title(title)
    ax.set_ylabel(y_label)
    ax.plot(x, y)
    ax.margins(x=0, y=0)

```


```python
fig, ax = plt.subplots()
plot(x, y1, ax, 'Increase in mean Fortune 500 company profits from 1955 to 2005', 'Profit (millions)')
```


    
![png](Readme_files/Readme_14_0.png)
    



```python
y2 = avgs.revenue
fig, ax = plt.subplots()
plot(x, y2, ax, 'Increase in mean Fortune 500 company revenues from 1955 to 2005', 'Revenue (millions)')
```


    
![png](Readme_files/Readme_15_0.png)
    



```python
def plot_with_std(x, y, stds, ax, title, y_label):
    ax.fill_between(x, y - stds, y + stds, alpha=0.2)
    plot(x, y, ax, title, y_label)
fig, (ax1, ax2) = plt.subplots(ncols=2)
title = 'Increase in mean and std Fortune 500 company %s from 1955 to 2005'
stds1 = group_by_year.std().profit.values
stds2 = group_by_year.std().revenue.values
plot_with_std(x, y1.values, stds1, ax1, title % 'profits', 'Profit (millions)')
plot_with_std(x, y2.values, stds2, ax2, title % 'revenues', 'Revenue (millions)')
fig.set_size_inches(14, 4)
fig.tight_layout()

```


    
![png](Readme_files/Readme_16_0.png)
    

拓展包安装期间我遇到了问题：![image](https://github.com/talerwang/Androidexam-21-041/assets/155062674/6801e0f5-d44b-4e5b-88e3-14771dbb4bb8)         


上网查询后，得知是jupyter的版本太高，降级后可用：![image](https://github.com/talerwang/Androidexam-21-041/assets/155062674/acfe314b-0dd9-493b-a7fa-4b883c48c81c)     



拓展包安装完毕：
![image](https://github.com/talerwang/Androidexam-21-041/assets/155062674/d92065dc-48f2-42d0-b696-9597c851af2b)


![image](https://github.com/talerwang/Androidexam-21-041/assets/155062674/19946e0b-278e-4bff-9727-d6477650d93d)


